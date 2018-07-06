package material.kangere.com.tandaza.NavActivities;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.RequestQueueSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateNote.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateNote extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView id;
    private EditText title,content;
    private String[] noteContent;
    private final String TAG = UpdateNote.class.getSimpleName();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public UpdateNote() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateNote.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateNote newInstance(String param1, String param2) {
        UpdateNote fragment = new UpdateNote();
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

        noteContent = getArguments().getStringArray("note_array");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_update_note, container, false);

        id = layout.findViewById(R.id.tvUpdateId);
        title = layout.findViewById(R.id.etUpdateTitle);
        content = layout.findViewById(R.id.etUpdateContent);
        Button update = layout.findViewById(R.id.bUpdate);

        title.setText(noteContent[0]);
        content.setText(noteContent[3]);
        id.setText(noteContent[5]);

        update.setOnClickListener(
                view -> updateNote()
        );
        return layout;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //volley operations
    private void updateNote() {

        //get text from edittexts to be updated in online database
        String post_id = id.getText().toString();
        String post_title = title.getText().toString();
        String post_content = content.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_NOTE,
                response -> {

                    Log.d(TAG, response);

                    int success = 0;
                    try {
                        JSONObject object = new JSONObject(response);
                        success = object.getInt("success");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    //check if result is successful
                    if(success == 1){

                        Toast.makeText(getActivity(),"Notification created successfully",Toast.LENGTH_LONG).show();

                        //go to notifications fragment to load updated data
                        Show_Notifications show_notifications = new Show_Notifications();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.flContent, show_notifications);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Unable to update Event",Toast.LENGTH_LONG).show();
                    }

                },
                error -> Log.e(TAG, error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("id",post_id);
                params.put("title",post_title);
                params.put("content",post_content);

                return params;
            }
        };

        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
