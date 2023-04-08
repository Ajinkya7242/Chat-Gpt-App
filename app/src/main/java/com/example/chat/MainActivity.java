package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomeTxt;
    EditText edtEnterMsg;
    ImageButton imgBtnSend;
    List<Message> messageList;
    messageAdapter adapter;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler);
        welcomeTxt=findViewById(R.id.welocmeTxt);
        edtEnterMsg=findViewById(R.id.edtMsg);
        imgBtnSend=findViewById(R.id.imageButtonEnter);
        messageList=new ArrayList<>();
       adapter=new messageAdapter(messageList);
       recyclerView.setAdapter(adapter);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=edtEnterMsg.getText().toString();
                if(!msg.isEmpty()){
                    welcomeTxt.setVisibility(View.GONE);
                    addToChat(msg,Message.SEND_BY_ME);

                    callApi(msg);

                    edtEnterMsg.setText("");
                }
            }
        });
    }

    void addToChat(String msg,String sendBy){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(msg,sendBy));
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });


    }

    void  addResponse(String response){
        messageList.remove(messageList.size()-1);
          addToChat(response,Message.SEND_BY_BOT);
    }

    void callApi(String question){

        messageList.add(new Message("Typing...",Message.SEND_BY_BOT));

        JSONObject jsonBody=new JSONObject();
        try {
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",question);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody requestBody=RequestBody.create(jsonBody.toString(),JSON);
        Request request=new Request.Builder().url("https://api.openai.com/v1/completions").header("Authorization","Bearer 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").post(requestBody).build();
          client.newCall(request).enqueue(new Callback() {
              @Override
              public void onFailure(@NonNull Call call, @NonNull IOException e) {
                     addResponse("Failed to Load Response Due To"+e.getMessage());

              }

              @Override
              public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                  if(response.isSuccessful()){

                      JSONObject jsonObject= null;
                      try {
                          jsonObject = new JSONObject(response.body().string());
                          JSONArray jsonArray=jsonObject.getJSONArray("choices");
                          String result=jsonArray.getJSONObject(0).getString("text");
                          addResponse(result.trim());
                      } catch (JSONException e) {
                          throw new RuntimeException(e);
                      }



                  }else{
                      addResponse("Failed to load Response"+response.body().string());
                  }

              }
          });
    }
}