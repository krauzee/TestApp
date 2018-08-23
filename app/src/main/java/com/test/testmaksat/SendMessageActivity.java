package com.test.testmaksat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.test.testmaksat.data.DataRepositoryImpl;
import com.test.testmaksat.data.DialogData;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiMessage;

import org.json.JSONArray;
import org.json.JSONException;

import rx.Single;

public class SendMessageActivity extends Activity {


    DialogData data;

    int id = 0;
    EditText text;
    Button send;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogs);

        data = (DialogData) getIntent().getSerializableExtra("data");

//        Arrays.sort(inList.toArray());
//        Arrays.sort(outList.toArray());

        text = findViewById(R.id.text);
        listView = findViewById(R.id.listMsg);
        listView.setAdapter(new DialogAdapter(data));

        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, id,
                        VKApiConst.MESSAGE, text.getText().toString()));
                text.setText("");


                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Toast.makeText(getApplicationContext(), "Сообщение отправлено!", Toast.LENGTH_LONG).show();
                        try {
                            JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                            VKApiMessage[] msg = new VKApiMessage[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                                msg[i] = mes;
                            }

                            for (VKApiMessage mess : msg) {
                                if (mess.out) {
                                    //outList.add(mess.body);

                                } else {
                                    //inList.add(mess.body);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
