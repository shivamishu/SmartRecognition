package com.sjsu.smartrecognition;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sjsu.smartrecognition.Network.AWSObjectAPI;
import com.sjsu.smartrecognition.Network.IAWSAPIService;
import com.sjsu.smartrecognition.databinding.FragmentResultBinding;
import com.sjsu.smartrecognition.model.ImageURI;
import com.sjsu.smartrecognition.model.Label;
import com.sjsu.smartrecognition.model.ObjectResponse;
import com.sjsu.smartrecognition.model.PostObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Result#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Result extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentResultBinding binding;
    private String base64String;
    private String mimeType;
    private String fileName;
    private IAWSAPIService mAWSAPIService;
    DecimalFormat df2 = new DecimalFormat("#.##");
    ArrayList<Label> data = new ArrayList<>();

    public Result() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Result.
     */
    // TODO: Rename and change types and number of parameters
    public static Result newInstance(String param1, String param2) {
        Result fragment = new Result();
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
//                String[] labelsArray = ResultArgs.fromBundle(getArguments()).getObjectLabelsArray();
//                String[] labelsRatingArray = ResultArgs.fromBundle(getArguments()).getObjectsRatingArray();
                ImageURI imageUri = ResultArgs.fromBundle(getArguments()).getImageUri();
                String user = ResultArgs.fromBundle(getArguments()).getUserName();
                user = user.replaceAll("\\s+", "");
                setFileName(user); // setting without extension. extension will be added in getBase64StringFromURI
                Uri photoUri = imageUri.getImageUri();
                executeImageComputations(photoUri);
//                int count = 0;
//                assert labelsArray != null;
//                for(String label : labelsArray){
//                    Label newLabel = new Label();
//                    newLabel.setName(label);
//                    assert labelsRatingArray != null;
//                    newLabel.setConfidence(Double.parseDouble(labelsRatingArray[count]));
//                    count++;
//                    data.add(newLabel);
//                }
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
        binding = FragmentResultBinding.inflate(inflater, container, false);
//        ArrayList<Label> data = new ArrayList<>();
//        binding.cardRecyclerView.setAdapter(new MainCardAdapter(requireActivity().getApplicationContext(), data));

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_result, container, false);
        binding.fab.setOnClickListener(new View.OnClickListener() {
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

//        @Override
//        public void onClick(View v) {
//            CheckBox cb = v.findViewById(R.id.checkBox);
//        }
    }

    public class MainCardAdapter extends RecyclerView.Adapter<Result.MainCardViewHolder> {
        private ArrayList<Label> dataList;
        private Context context;

        public MainCardAdapter(Context context, ArrayList<Label> dataList) {
            this.context = context;
            this.dataList = dataList;
        }


        @NonNull
        @Override
        public MainCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout itemView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.detection_item, parent, false);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    CheckBox cb = v.findViewById(R.id.checkBox);
//                    cb.toggle();
//                }
//            });
            return new MainCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MainCardViewHolder holder, int position) {
            Label item = dataList.get(position);
            holder.titleView.setText(item.getName());
            holder.rateView.setText(df2.format(item.getConfidence()));
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

    private void executeImageComputations(Uri photoUri) {
        // Load the image located at photoUri into selectedImage
        Bitmap selectedImage = loadFromUri(photoUri);
        String imageBase64String = getBase64StringFromURI(photoUri, selectedImage);
        setBase64String(imageBase64String);
        callAPI();
    }

    private String getBase64StringFromURI(Uri photoUri, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.upload);
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
        setFileName(getFileName() + "." + ext);   //eg: upload.png
        setMimeType(mimeType);  //eg: image/png
        Bitmap.CompressFormat compressFormat;
        if (mimeType.equals("image/png")) {
            compressFormat = Bitmap.CompressFormat.PNG;
        } else {
            compressFormat = Bitmap.CompressFormat.JPEG;
        }
        bitmap.compress(compressFormat, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
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

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void callAPI() {
        mAWSAPIService = AWSObjectAPI.getAPIService();
        String imageBase64String = getBase64String();
        String imageFileName = getFileName();
        String imageMimeType = getMimeType();

        mAWSAPIService.sendPhoto(imageBase64String, imageFileName, imageMimeType).enqueue(new Callback<PostObject>() {
            @Override
            public void onResponse(Call<PostObject> call, Response<PostObject> response) {

                if (response.isSuccessful()) {
                    handleResponse(response.body().toString());
                    Log.i("RESPONSE", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<PostObject> call, Throwable t) {
                Log.e("ERROR", "Unable to submit post to API.");
            }
        });
        ;
    }

    public void handleResponse(String response) {

//            String[] resultLabels = null;
//            String[] resultRatings = null;
        Gson gson = new Gson();
//            String jsonString = "{\"Labels\":[{\"Name\":\"Car\",\"Confidence\":98.87620544433594,\"Instances\":[{\"BoundingBox\":{\"Width\":0.10527370870113373,\"Height\":0.1847248375415802,\"Left\":0.004289206117391586,\"Top\":0.5051581859588623},\"Confidence\":98.87620544433594},{\"BoundingBox\":{\"Width\":0.240233913064003,\"Height\":0.21589209139347076,\"Left\":0.7305676937103271,\"Top\":0.5268267393112183},\"Confidence\":98.48017883300781},{\"BoundingBox\":{\"Width\":0.1407817304134369,\"Height\":0.15459850430488586,\"Left\":0.6501889228820801,\"Top\":0.532597541809082},\"Confidence\":98.07795715332031},{\"BoundingBox\":{\"Width\":0.10888396203517914,\"Height\":0.10217276215553284,\"Left\":0.10204391926527023,\"Top\":0.5367318987846375},\"Confidence\":96.16504669189453},{\"BoundingBox\":{\"Width\":0.05833936110138893,\"Height\":0.05552774667739868,\"Left\":0.46539855003356934,\"Top\":0.5593809485435486},\"Confidence\":94.30266571044922},{\"BoundingBox\":{\"Width\":0.028117168694734573,\"Height\":0.19196289777755737,\"Left\":0,\"Top\":0.5102758407592773},\"Confidence\":91.85574340820312},{\"BoundingBox\":{\"Width\":0.05696670338511467,\"Height\":0.16707178950309753,\"Left\":0.9425616264343262,\"Top\":0.5257886648178101},\"Confidence\":90.01618194580078},{\"BoundingBox\":{\"Width\":0.10661390423774719,\"Height\":0.1210075095295906,\"Left\":0.5679569840431213,\"Top\":0.5334048271179199},\"Confidence\":89.92594909667969},{\"BoundingBox\":{\"Width\":0.06029858812689781,\"Height\":0.06782551109790802,\"Left\":0.223601832985878,\"Top\":0.5438868999481201},\"Confidence\":89.3087158203125},{\"BoundingBox\":{\"Width\":0.041884686797857285,\"Height\":0.03382435441017151,\"Left\":0.3147863447666168,\"Top\":0.5573541522026062},\"Confidence\":86.47835540771484},{\"BoundingBox\":{\"Width\":0.05941527709364891,\"Height\":0.0942845568060875,\"Left\":0.1709432154893875,\"Top\":0.5349381566047668},\"Confidence\":78.93329620361328},{\"BoundingBox\":{\"Width\":0.03125765919685364,\"Height\":0.042590539902448654,\"Left\":0.28353965282440186,\"Top\":0.5553879141807556},\"Confidence\":78.4329605102539},{\"BoundingBox\":{\"Width\":0.10955511033535004,\"Height\":0.15647290647029877,\"Left\":0.8899731636047363,\"Top\":0.5232131481170654},\"Confidence\":61.89860153198242},{\"BoundingBox\":{\"Width\":0.028528062626719475,\"Height\":0.05612713471055031,\"Left\":0.26153871417045593,\"Top\":0.5507346987724304},\"Confidence\":60.06472396850586}],\"Parents\":[{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Automobile\",\"Confidence\":98.87620544433594,\"Instances\":[],\"Parents\":[{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Vehicle\",\"Confidence\":98.87620544433594,\"Instances\":[],\"Parents\":[{\"Name\":\"Transportation\"}]},{\"Name\":\"Transportation\",\"Confidence\":98.87620544433594,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Person\",\"Confidence\":98.37577819824219,\"Instances\":[{\"BoundingBox\":{\"Width\":0.19036127626895905,\"Height\":0.27238351106643677,\"Left\":0.43754449486732483,\"Top\":0.35202959179878235},\"Confidence\":98.37577819824219},{\"BoundingBox\":{\"Width\":0.037608712911605835,\"Height\":0.06765095144510269,\"Left\":0.9162867665290833,\"Top\":0.5000146627426147},\"Confidence\":86.0064697265625}],\"Parents\":[]},{\"Name\":\"Human\",\"Confidence\":98.37577819824219,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Pedestrian\",\"Confidence\":97.18687438964844,\"Instances\":[],\"Parents\":[{\"Name\":\"Person\"}]},{\"Name\":\"Skateboard\",\"Confidence\":94.39463806152344,\"Instances\":[{\"BoundingBox\":{\"Width\":0.12381358444690704,\"Height\":0.05817228928208351,\"Left\":0.4477302134037018,\"Top\":0.633576512336731},\"Confidence\":94.39463806152344}],\"Parents\":[{\"Name\":\"Sport\"},{\"Name\":\"Person\"}]},{\"Name\":\"Sport\",\"Confidence\":94.39463806152344,\"Instances\":[],\"Parents\":[{\"Name\":\"Person\"}]},{\"Name\":\"Sports\",\"Confidence\":94.39463806152344,\"Instances\":[],\"Parents\":[{\"Name\":\"Person\"}]},{\"Name\":\"Road\",\"Confidence\":92.47262573242188,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Wheel\",\"Confidence\":90.8355712890625,\"Instances\":[{\"BoundingBox\":{\"Width\":0.03647598996758461,\"Height\":0.08830293267965317,\"Left\":0.7853870987892151,\"Top\":0.6465479135513306},\"Confidence\":90.8355712890625},{\"BoundingBox\":{\"Width\":0.009989958256483078,\"Height\":0.01862962171435356,\"Left\":0.2806642949581146,\"Top\":0.5828559398651123},\"Confidence\":89.3956069946289},{\"BoundingBox\":{\"Width\":0.023195521906018257,\"Height\":0.07285896688699722,\"Left\":0.04648001864552498,\"Top\":0.6141234040260315},\"Confidence\":85.24990844726562},{\"BoundingBox\":{\"Width\":0.01864992454648018,\"Height\":0.06884101033210754,\"Left\":0.6836425065994263,\"Top\":0.6229138374328613},\"Confidence\":79.96819305419922},{\"BoundingBox\":{\"Width\":0.020315494388341904,\"Height\":0.05227087065577507,\"Left\":0.08717165887355804,\"Top\":0.618294358253479},\"Confidence\":72.9918212890625},{\"BoundingBox\":{\"Width\":0.02088242955505848,\"Height\":0.07131772488355637,\"Left\":0.7295734286308289,\"Top\":0.6350470185279846},\"Confidence\":72.71833038330078},{\"BoundingBox\":{\"Width\":0.016419032588601112,\"Height\":0.04919019341468811,\"Left\":0.6454715132713318,\"Top\":0.6280504465103149},\"Confidence\":71.74444580078125},{\"BoundingBox\":{\"Width\":0.014713042415678501,\"Height\":0.04130025580525398,\"Left\":0.594515323638916,\"Top\":0.6118574142456055},\"Confidence\":62.49579620361328},{\"BoundingBox\":{\"Width\":0.03588014841079712,\"Height\":0.04596918448805809,\"Left\":0.9257962107658386,\"Top\":0.702854573726654},\"Confidence\":60.99583053588867},{\"BoundingBox\":{\"Width\":0.008641310967504978,\"Height\":0.01863808184862137,\"Left\":0.2643525004386902,\"Top\":0.5905612707138062},\"Confidence\":53.09222412109375}],\"Parents\":[{\"Name\":\"Machine\"}]},{\"Name\":\"Machine\",\"Confidence\":90.8355712890625,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Path\",\"Confidence\":90.75850677490234,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Downtown\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[{\"Name\":\"City\"},{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"City\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Urban\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Building\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Town\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Tarmac\",\"Confidence\":86.15850067138672,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Asphalt\",\"Confidence\":86.15850067138672,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Parking Lot\",\"Confidence\":85.47086334228516,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Parking\",\"Confidence\":85.47086334228516,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Intersection\",\"Confidence\":84.87483978271484,\"Instances\":[],\"Parents\":[{\"Name\":\"Road\"}]},{\"Name\":\"Architecture\",\"Confidence\":80.8434066772461,\"Instances\":[],\"Parents\":[{\"Name\":\"Building\"}]},{\"Name\":\"Office Building\",\"Confidence\":62.91800308227539,\"Instances\":[],\"Parents\":[{\"Name\":\"Building\"}]},{\"Name\":\"Sidewalk\",\"Confidence\":62.8677864074707,\"Instances\":[],\"Parents\":[{\"Name\":\"Path\"}]},{\"Name\":\"Pavement\",\"Confidence\":62.8677864074707,\"Instances\":[],\"Parents\":[{\"Name\":\"Path\"}]},{\"Name\":\"Neighborhood\",\"Confidence\":59.80064392089844,\"Instances\":[],\"Parents\":[{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Street\",\"Confidence\":56.94782257080078,\"Instances\":[],\"Parents\":[{\"Name\":\"City\"},{\"Name\":\"Road\"},{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Coupe\",\"Confidence\":56.08636474609375,\"Instances\":[],\"Parents\":[{\"Name\":\"Sports Car\"},{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Sports Car\",\"Confidence\":56.08636474609375,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Sedan\",\"Confidence\":55.48483657836914,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]}],\"LabelModelVersion\":\"2.0\"}";
        ObjectResponse result = gson.fromJson(response, ObjectResponse.class);
        if (result != null) {
            setData(result.getLabels());
            binding.cardRecyclerView.setAdapter(new MainCardAdapter(requireActivity().getApplicationContext(), getData()));
//                resultLabels = new String[labels.size()];
//                resultRatings = new String[labels.size()];
//                int index = 0;
//                DecimalFormat df2 = new DecimalFormat("#.##");
//                for (Label label : labels) {
//                    resultLabels[index] = label.getName();
//                    resultRatings[index] = df2.format(label.getConfidence()).toString();
//                    index++;
//                }
        }

//            return new Object[]{resultLabels, resultRatings};


    }

    public void setData(ArrayList<Label> labels) {
        this.data = labels;
    }

    public ArrayList<Label> getData() {
        return data;
    }
}