package com.sjsu.smartrecognition;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.sjsu.smartrecognition.Network.AWSTextAPI;
import com.sjsu.smartrecognition.Network.IAWSAPITextService;
import com.sjsu.smartrecognition.databinding.FragmentTextResultBinding;
import com.sjsu.smartrecognition.model.BoundingBox;
import com.sjsu.smartrecognition.model.ImageURI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.ResponseText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TextResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextResult extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentTextResultBinding binding;
    private String fileName;
    private String title;
    private Bitmap bitmap;
    private IAWSAPITextService mAWSAPITextService;
    DecimalFormat df2 = new DecimalFormat("#.##");
    ArrayList<model.TextDetection> data = new ArrayList<>();

    static TransferUtility transferUtility;
    // A List of all transfers
    static List<TransferObserver> observers;

    public TextResult() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TextResult.
     */
    // TODO: Rename and change types and number of parameters
    public static TextResult newInstance(String param1, String param2) {
        TextResult fragment = new TextResult();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                ImageURI imageUri = TextResultArgs.fromBundle(getArguments()).getImageUri();
                String user = TextResultArgs.fromBundle(getArguments()).getUserName();
                user = user.replaceAll("\\s+", "");
                setFileName(user); // setting without extension. extension will be added in getBase64StringFromURI
                Uri photoUri = imageUri.getImageUri();
                String mode = TextResultArgs.fromBundle(getArguments()).getTitle();
                setTitle(mode);
                executeImageComputations(photoUri);
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        } catch (AssertionError err) {
            //handle something here
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTextResultBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_result, container, false);
        String modeTitle = String.format(getString(R.string.results), getTitle());
        binding.progressBar2.setVisibility(View.VISIBLE);
        binding.results3.setText(modeTitle);
        binding.fab3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_result_to_selection);
            }
        });
        return binding.getRoot();
    }

    public class MainCardViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView rateView;

        public MainCardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            rateView = itemView.findViewById(R.id.rate);

        }
    }

    public class MainCardAdapter extends RecyclerView.Adapter<TextResult.MainCardViewHolder> {
        private ArrayList<model.TextDetection> dataList;
        private Context context;

        public MainCardAdapter(Context context, ArrayList<model.TextDetection> dataList) {
            this.context = context;
            this.dataList = dataList;
        }


        @NonNull
        @Override
        public MainCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout itemView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.detection_item, parent, false);
            return new MainCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MainCardViewHolder holder, int position) {
            model.TextDetection item = dataList.get(position);
            holder.titleView.setText(item.getDetectedText());
            holder.rateView.setText(df2.format(item.getConfidence()) + " %");

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getApplicationContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(requireActivity().getApplicationContext().getContentResolver(), photoUri);
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
        return image;
    }

    private void prepareCanvasDrawing() {
        Bitmap btm = getBitMapImage();
        int imageWidth = btm.getWidth();
        int imageHeight = btm.getHeight();
        ArrayList<model.TextDetection> texts = getData();
        for (model.TextDetection text : texts) {
            model.Geometry geometry = text.getGeometry();
            if (geometry != null & text.getConfidence() > 90) {
                BoundingBox boundBox = geometry.getBoundingBox();
                drawBoundBox(getBitMapImage(), (Double) boundBox.getLeft(), (Double) boundBox.getTop(), (Double) boundBox.getWidth(), (Double) boundBox.getHeight(), imageHeight, imageWidth);
            }
        }
    }

    private void drawBoundBox(Bitmap bitmap, Double l, Double t, Double r, Double b, int imageHeight, int imageWidth) {
        bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        float left = (float) (imageWidth * l);
        float top = (float) (imageHeight * t);
        float right = (float) (imageWidth * r) + left;
        float bottom = (float) (imageHeight * b) + top;
//        float left = 328;
//        float top = (float) 0.5;
//        float right = 676;
//        float bottom = 715;
        Log.i("left, top, right, bottom", left + ", " + top + ", " + right + ", " + bottom);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAlpha(0xA0); // the transparency
        paint.setColor(Color.GREEN); // color is red
        paint.setStyle(Paint.Style.STROKE); // stroke or fill or ...
        paint.setStrokeWidth(3f); // the stroke width
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRect(rect, paint);
        setBitMapImage(bitmap);
    }

    private void executeImageComputations(Uri photoUri) {
        File file = null;
        try {
            file = readContentToFile(photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file != null) {
            beginUpload(file);
            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);
            setBitMapImage(selectedImage);
        } else {
            Toast.makeText(requireActivity().getApplicationContext(), "An error occurred. Please upload the photo again", Toast.LENGTH_LONG).show();
        }

    }

    private void beginUpload(File file) {
//        TransferObserver observer = transferUtility.upload(
//                file.getName(),
//                file
//        );
        BasicAWSCredentials credentials = new BasicAWSCredentials(BuildConfig.KEY, BuildConfig.SECRET);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        s3Client.setEndpoint("s3.us-west-1.amazonaws.com");
//        TransferUtility transferUtility =
//                TransferUtility.builder()
//                        .context(requireActivity().getApplicationContext())
//                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
//                        .s3Client(s3Client)
//                        .build();
        TransferUtility transferUtility = new TransferUtility(s3Client, requireActivity().getApplicationContext());
        TransferObserver uploadObserver =
                transferUtility.upload(BuildConfig.BUCKET, getFileName(), file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed download.
//                    Toast.makeText(requireActivity().getApplicationContext(), "Photo uploaded successfully!", Toast.LENGTH_LONG).show();
                    callAPI();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                binding.progressBar2.setVisibility(View.GONE);
                Toast.makeText(requireActivity().getApplicationContext(), "An error occurred. Please upload the photo again", Toast.LENGTH_LONG).show();
            }

        });

        // If the upload does not trigger the onStateChanged method inside
        // TransferListener, we can directly check the transfer state as shown here.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
            //not required as of now
        }
    }

    private File readContentToFile(Uri photoUri) throws IOException {
        String ext = "png";
        String mimeType;
        if (photoUri != null) {
            ContentResolver cR = getContext().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            ext = mime.getExtensionFromMimeType(cR.getType(photoUri));
            mimeType = getMimeType("upload." + ext);
        } else {
            mimeType = getMimeType("upload.png");
        }
        Date d = new Date();
        long timeMilli = d.getTime();
        String completeFileName = getFileName() + "_" + String.valueOf(timeMilli) + "." + ext;  //uniqueFileName per user
        setFileName(completeFileName);   //eg: ShivamShrivastav_1508484583259.png
//        setMimeType(mimeType);  //eg: image/png
        final File file = new File(requireActivity().getApplicationContext().getCacheDir(), completeFileName);
        try (
                final InputStream in = getContext().getContentResolver().openInputStream(photoUri);
                final OutputStream out = new FileOutputStream(file, false);
        ) {
            byte[] buffer = new byte[1024];
            for (int len; (len = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, len);
            }
            return file;
        }
    }

    public static String getMimeType(String url) {
        String mimeType;
        mimeType = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    private void callAPI() {
        mAWSAPITextService = AWSTextAPI.getAPIService();
        String imageFileName = getFileName();
        String recMode = getTitle().toUpperCase();

        mAWSAPITextService.recognize(imageFileName, recMode).enqueue(new Callback<ResponseText>() {
            @Override
            public void onResponse(Call<ResponseText> call, Response<ResponseText> response) {

                if (response.isSuccessful()) {
                    handleResponse(response.body().getTextDetections());
                    binding.progressBar2.setVisibility(View.GONE);
                    Log.i("RESPONSE", "got results from API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseText> call, Throwable t) {
                binding.progressBar2.setVisibility(View.GONE);
                Log.e("ERROR", "Error occurred calling API.");
            }
        });
        ;
    }

    public void handleResponse(ArrayList<model.TextDetection> response) {
        if (response != null) {
            setData(response);
            prepareCanvasDrawing();
            binding.cardRecyclerView3.setAdapter(new MainCardAdapter(requireActivity().getApplicationContext(), getData()));
        }
        binding.resultImageView3.setImageBitmap(getBitMapImage());
    }

    public void setData(ArrayList<model.TextDetection> texts) {
        this.data = texts;
    }

    public ArrayList<model.TextDetection> getData() {
        return data;
    }

    public void setBitMapImage(Bitmap btm) {
        this.bitmap = btm;
    }

    public Bitmap getBitMapImage() {
        return bitmap;
    }

}