package com.sjsu.smartrecognition;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.sjsu.smartrecognition.databinding.FragmentSelectionBinding;
import com.sjsu.smartrecognition.model.ImageURI;
import com.sjsu.smartrecognition.model.Label;
import com.sjsu.smartrecognition.model.ObjectResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Selection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Selection extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSelectionBinding binding;
    private String mode = "Objects";
//    private String base64String;
    private ImageURI imageURI;
    private static final int IMAGE_REQUEST_CODE = 0612;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Selection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Selection.
     */
    // TODO: Rename and change types and number of parameters
    public static Selection newInstance(String param1, String param2) {
        Selection fragment = new Selection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectionBinding.inflate(inflater, container, false);
//        return inflater.inflate(R.layout.fragment_selection, container, false);

        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
//                Chip chip = chipGroup.findViewById(checkedId);
//                String mode = (String) chip.getText();
                mode = getSelectedMode();
                binding.recognize.setText("Recognize " + mode);
            }

            public void onClick(View v) {
                changeRecognizeText();
            }
        });

        binding.reset.setVisibility(View.GONE);
        binding.reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetImage();
            }
        });

        binding.uploadImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openImageIntent();
            }
        });


        binding.recognize.setText("Recognize Objects");

        binding.recognize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                computeRecognition(v);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && requestCode == IMAGE_REQUEST_CODE) {
            binding.reset.setVisibility(View.VISIBLE);
            Uri photoUri = data.getData();
            imageURI.setImageUri(photoUri);
            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);
            // Load the selected image into a preview
            binding.uploadImageView.setImageBitmap(selectedImage);
//            base64String = getBase64String(photoUri, selectedImage);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
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

    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    private void openImageIntent() {

        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST_CODE);
        }
        //        Intent imageOpener = new Intent(Intent.ACTION_SEND);
//        imageOpener.setType("image/*");
//        if (imageOpener != null) {
//            startActivityForResult(imageOpener, IMAGE_REQUEST_CODE);
//        } else {
//            Toast.makeText(this, "Please allow photos permission", Toast.LENGTH_SHORT).show();
//        }
    }

    private void computeRecognition(View view) {
//        Object[] resultObjects = printSampleResponse();
//        SelectionDirections.ActionSelectionToResult action =  SelectionDirections.actionSelectionToResult((String[]) resultObjects[0], (String[]) resultObjects[1]);
//        action.setObjectLabelsArray((String[]) resultObjects[0]);
//        action.setObjectsRatingArray((String[]) resultObjects[1]);
        if(imageURI.getImageUri() != null){
            String userName = (String) MainActivity.getUserName();
            SelectionDirections.ActionSelectionToResult action = SelectionDirections.actionSelectionToResult(imageURI, userName);
            action.setImageUri(imageURI);
            action.setUserName(userName);
            Navigation.findNavController(view).navigate(action);
        }else{
            Toast.makeText(requireActivity().getApplicationContext(), "Please upload photo to proceed", Toast.LENGTH_LONG).show();
        }


    }

    private void resetImage() {
        binding.uploadImageView.setImageResource(R.drawable.upload);
//        base64String = null;  //reset previous string to null
        imageURI.setImageUri(null);  //reset previous string to null
        binding.reset.setVisibility(View.GONE);

    }

    private String getSelectedMode() {
        int i = 0;
        String checked = "Objects";
        while (i < 3) {
            Chip chip = (Chip) binding.chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                switch (i) {
                    case 1:
                        checked = "Text";
                        break;
                    case 2:
                        checked = "PPE Kit";
                        break;
                    default:
                        checked = "Objects";
                        break;
                }
            }
            i++;
        }
        ;
        return checked;
    }

    private void changeRecognizeText() {
        String mode = getSelectedMode();
        binding.recognize.setText("Recognize " + mode);
    }

    private String getBase64String(Uri photoUri, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.upload);
        String ext;
        String mimeType;
        if (photoUri != null) {
            ContentResolver cR = getContext().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            ext = mime.getExtensionFromMimeType(cR.getType(photoUri));
            mimeType = getMimeType("upload." + ext);
        } else {
            mimeType = getMimeType("upload.png");
        }
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

//    public String getPhotoBase64String() {
//        return base64String;
//    }

    public Object[] printSampleResponse() {
        String[] resultLabels = null;
        String[] resultRatings = null;
        Gson gson = new Gson();
        String jsonString = "{\"Labels\":[{\"Name\":\"Car\",\"Confidence\":98.87620544433594,\"Instances\":[{\"BoundingBox\":{\"Width\":0.10527370870113373,\"Height\":0.1847248375415802,\"Left\":0.004289206117391586,\"Top\":0.5051581859588623},\"Confidence\":98.87620544433594},{\"BoundingBox\":{\"Width\":0.240233913064003,\"Height\":0.21589209139347076,\"Left\":0.7305676937103271,\"Top\":0.5268267393112183},\"Confidence\":98.48017883300781},{\"BoundingBox\":{\"Width\":0.1407817304134369,\"Height\":0.15459850430488586,\"Left\":0.6501889228820801,\"Top\":0.532597541809082},\"Confidence\":98.07795715332031},{\"BoundingBox\":{\"Width\":0.10888396203517914,\"Height\":0.10217276215553284,\"Left\":0.10204391926527023,\"Top\":0.5367318987846375},\"Confidence\":96.16504669189453},{\"BoundingBox\":{\"Width\":0.05833936110138893,\"Height\":0.05552774667739868,\"Left\":0.46539855003356934,\"Top\":0.5593809485435486},\"Confidence\":94.30266571044922},{\"BoundingBox\":{\"Width\":0.028117168694734573,\"Height\":0.19196289777755737,\"Left\":0,\"Top\":0.5102758407592773},\"Confidence\":91.85574340820312},{\"BoundingBox\":{\"Width\":0.05696670338511467,\"Height\":0.16707178950309753,\"Left\":0.9425616264343262,\"Top\":0.5257886648178101},\"Confidence\":90.01618194580078},{\"BoundingBox\":{\"Width\":0.10661390423774719,\"Height\":0.1210075095295906,\"Left\":0.5679569840431213,\"Top\":0.5334048271179199},\"Confidence\":89.92594909667969},{\"BoundingBox\":{\"Width\":0.06029858812689781,\"Height\":0.06782551109790802,\"Left\":0.223601832985878,\"Top\":0.5438868999481201},\"Confidence\":89.3087158203125},{\"BoundingBox\":{\"Width\":0.041884686797857285,\"Height\":0.03382435441017151,\"Left\":0.3147863447666168,\"Top\":0.5573541522026062},\"Confidence\":86.47835540771484},{\"BoundingBox\":{\"Width\":0.05941527709364891,\"Height\":0.0942845568060875,\"Left\":0.1709432154893875,\"Top\":0.5349381566047668},\"Confidence\":78.93329620361328},{\"BoundingBox\":{\"Width\":0.03125765919685364,\"Height\":0.042590539902448654,\"Left\":0.28353965282440186,\"Top\":0.5553879141807556},\"Confidence\":78.4329605102539},{\"BoundingBox\":{\"Width\":0.10955511033535004,\"Height\":0.15647290647029877,\"Left\":0.8899731636047363,\"Top\":0.5232131481170654},\"Confidence\":61.89860153198242},{\"BoundingBox\":{\"Width\":0.028528062626719475,\"Height\":0.05612713471055031,\"Left\":0.26153871417045593,\"Top\":0.5507346987724304},\"Confidence\":60.06472396850586}],\"Parents\":[{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Automobile\",\"Confidence\":98.87620544433594,\"Instances\":[],\"Parents\":[{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Vehicle\",\"Confidence\":98.87620544433594,\"Instances\":[],\"Parents\":[{\"Name\":\"Transportation\"}]},{\"Name\":\"Transportation\",\"Confidence\":98.87620544433594,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Person\",\"Confidence\":98.37577819824219,\"Instances\":[{\"BoundingBox\":{\"Width\":0.19036127626895905,\"Height\":0.27238351106643677,\"Left\":0.43754449486732483,\"Top\":0.35202959179878235},\"Confidence\":98.37577819824219},{\"BoundingBox\":{\"Width\":0.037608712911605835,\"Height\":0.06765095144510269,\"Left\":0.9162867665290833,\"Top\":0.5000146627426147},\"Confidence\":86.0064697265625}],\"Parents\":[]},{\"Name\":\"Human\",\"Confidence\":98.37577819824219,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Pedestrian\",\"Confidence\":97.18687438964844,\"Instances\":[],\"Parents\":[{\"Name\":\"Person\"}]},{\"Name\":\"Skateboard\",\"Confidence\":94.39463806152344,\"Instances\":[{\"BoundingBox\":{\"Width\":0.12381358444690704,\"Height\":0.05817228928208351,\"Left\":0.4477302134037018,\"Top\":0.633576512336731},\"Confidence\":94.39463806152344}],\"Parents\":[{\"Name\":\"Sport\"},{\"Name\":\"Person\"}]},{\"Name\":\"Sport\",\"Confidence\":94.39463806152344,\"Instances\":[],\"Parents\":[{\"Name\":\"Person\"}]},{\"Name\":\"Sports\",\"Confidence\":94.39463806152344,\"Instances\":[],\"Parents\":[{\"Name\":\"Person\"}]},{\"Name\":\"Road\",\"Confidence\":92.47262573242188,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Wheel\",\"Confidence\":90.8355712890625,\"Instances\":[{\"BoundingBox\":{\"Width\":0.03647598996758461,\"Height\":0.08830293267965317,\"Left\":0.7853870987892151,\"Top\":0.6465479135513306},\"Confidence\":90.8355712890625},{\"BoundingBox\":{\"Width\":0.009989958256483078,\"Height\":0.01862962171435356,\"Left\":0.2806642949581146,\"Top\":0.5828559398651123},\"Confidence\":89.3956069946289},{\"BoundingBox\":{\"Width\":0.023195521906018257,\"Height\":0.07285896688699722,\"Left\":0.04648001864552498,\"Top\":0.6141234040260315},\"Confidence\":85.24990844726562},{\"BoundingBox\":{\"Width\":0.01864992454648018,\"Height\":0.06884101033210754,\"Left\":0.6836425065994263,\"Top\":0.6229138374328613},\"Confidence\":79.96819305419922},{\"BoundingBox\":{\"Width\":0.020315494388341904,\"Height\":0.05227087065577507,\"Left\":0.08717165887355804,\"Top\":0.618294358253479},\"Confidence\":72.9918212890625},{\"BoundingBox\":{\"Width\":0.02088242955505848,\"Height\":0.07131772488355637,\"Left\":0.7295734286308289,\"Top\":0.6350470185279846},\"Confidence\":72.71833038330078},{\"BoundingBox\":{\"Width\":0.016419032588601112,\"Height\":0.04919019341468811,\"Left\":0.6454715132713318,\"Top\":0.6280504465103149},\"Confidence\":71.74444580078125},{\"BoundingBox\":{\"Width\":0.014713042415678501,\"Height\":0.04130025580525398,\"Left\":0.594515323638916,\"Top\":0.6118574142456055},\"Confidence\":62.49579620361328},{\"BoundingBox\":{\"Width\":0.03588014841079712,\"Height\":0.04596918448805809,\"Left\":0.9257962107658386,\"Top\":0.702854573726654},\"Confidence\":60.99583053588867},{\"BoundingBox\":{\"Width\":0.008641310967504978,\"Height\":0.01863808184862137,\"Left\":0.2643525004386902,\"Top\":0.5905612707138062},\"Confidence\":53.09222412109375}],\"Parents\":[{\"Name\":\"Machine\"}]},{\"Name\":\"Machine\",\"Confidence\":90.8355712890625,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Path\",\"Confidence\":90.75850677490234,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Downtown\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[{\"Name\":\"City\"},{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"City\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Urban\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Building\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Town\",\"Confidence\":89.85538482666016,\"Instances\":[],\"Parents\":[{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Tarmac\",\"Confidence\":86.15850067138672,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Asphalt\",\"Confidence\":86.15850067138672,\"Instances\":[],\"Parents\":[]},{\"Name\":\"Parking Lot\",\"Confidence\":85.47086334228516,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Parking\",\"Confidence\":85.47086334228516,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Intersection\",\"Confidence\":84.87483978271484,\"Instances\":[],\"Parents\":[{\"Name\":\"Road\"}]},{\"Name\":\"Architecture\",\"Confidence\":80.8434066772461,\"Instances\":[],\"Parents\":[{\"Name\":\"Building\"}]},{\"Name\":\"Office Building\",\"Confidence\":62.91800308227539,\"Instances\":[],\"Parents\":[{\"Name\":\"Building\"}]},{\"Name\":\"Sidewalk\",\"Confidence\":62.8677864074707,\"Instances\":[],\"Parents\":[{\"Name\":\"Path\"}]},{\"Name\":\"Pavement\",\"Confidence\":62.8677864074707,\"Instances\":[],\"Parents\":[{\"Name\":\"Path\"}]},{\"Name\":\"Neighborhood\",\"Confidence\":59.80064392089844,\"Instances\":[],\"Parents\":[{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Street\",\"Confidence\":56.94782257080078,\"Instances\":[],\"Parents\":[{\"Name\":\"City\"},{\"Name\":\"Road\"},{\"Name\":\"Urban\"},{\"Name\":\"Building\"}]},{\"Name\":\"Coupe\",\"Confidence\":56.08636474609375,\"Instances\":[],\"Parents\":[{\"Name\":\"Sports Car\"},{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Sports Car\",\"Confidence\":56.08636474609375,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]},{\"Name\":\"Sedan\",\"Confidence\":55.48483657836914,\"Instances\":[],\"Parents\":[{\"Name\":\"Car\"},{\"Name\":\"Vehicle\"},{\"Name\":\"Transportation\"}]}],\"LabelModelVersion\":\"2.0\"}";
        ObjectResponse result = gson.fromJson(jsonString, ObjectResponse.class);
        if (result != null) {
            List<Label> labels = result.getLabels();
            resultLabels = new String[labels.size()];
            resultRatings = new String[labels.size()];
            int index = 0;
            DecimalFormat df2 = new DecimalFormat("#.##");
            for (Label label : labels) {
                resultLabels[index] = label.getName();
                resultRatings[index] = df2.format(label.getConfidence()).toString();
                index++;
            }
        }

        return new Object[]{resultLabels, resultRatings};

    }
}