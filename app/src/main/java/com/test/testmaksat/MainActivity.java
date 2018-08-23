package com.test.testmaksat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.test.testmaksat.data.DataRepositoryImpl;
import com.test.testmaksat.data.DialogData;
import com.test.testmaksat.data.UserDialogsListData;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

import rx.Observable;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity implements DialogsAdapter.OnDialogClickListener {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    private ListView listFriend;
    private Button showMessage;
    private VKList list;
    private DataRepositoryImpl dataRepository = new DataRepositoryImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKSdk.login(this, scope);

        listFriend = (ListView) findViewById(R.id.listfriends);
        showMessage = (Button) findViewById(R.id.showMessage);
        showMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dataRepository.getUsersData().subscribe(new Action1<UserDialogsListData>() {
                    @Override
                    public void call(UserDialogsListData userDialogsListData) {
                        listFriend.setAdapter(new DialogsAdapter(userDialogsListData, MainActivity.this));
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался

                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name, last_name"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        list = (VKList) response.parsedModel;
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1, list);

                        listFriend.setAdapter(arrayAdapter);
                    }
                });

                Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDialogClick(int userId) {
        //todo показывать прогресс
        dataRepository.getMessagesData(userId).subscribe(new Action1<DialogData>() {
            @Override
            public void call(DialogData dialogData) {
                startActivity(new Intent(MainActivity.this, SendMessageActivity.class).putExtra("data", dialogData));
            }
        });
    }
}
