package com.test.testmaksat.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.os.Message;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;
import org.json.JSONArray;
import org.json.JSONException;
import rx.Single;
import rx.SingleSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

public class DataRepositoryImpl {

    public Boolean isOnline (Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;

    }



 /*   public interface UserDialogsListDataDAO {

        @Insert
        void insertAll(UserDialogsListData userDialogsListData);


        @Query("SELECT * FROM userdata")
        List<UserDialogsListData.UserData> getAll();

    }

    @Database(entities = {UserDialogsListData.UserData.class}, version = 1)
    public abstract class DataBase extends RoomDatabase {
        public abstract com.test.testmaksat.data.UserDialogsListDataDAO userDialogsListDataDAO();
    }
 */



    public Single<UserDialogsListData> getUsersData() {
        return Single.create(new Single.OnSubscribe<UserDialogsListData>() {
            @Override
            public void call(final SingleSubscriber<? super UserDialogsListData> singleSubscriber) {
                //todo найти проверку,есть ли интернет
                if (isOnline(getApplicationContext())){

                    final VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 30));
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {

                            super.onComplete(response);

                            VKApiGetDialogResponse getDialogResponse = (VKApiGetDialogResponse) response.parsedModel;

                            VKList<VKApiDialog> list = getDialogResponse.items;

                            List<UserDialogsListData.UserData> data = new ArrayList<>();

                            for (VKApiDialog msg : list) {
                                data.add(new UserDialogsListData.UserData(
                                        String.valueOf(list.getById(msg.message.user_id)),
                                        msg.message.user_id,
                                        msg.message.body
                                ));
                            }
                            //todo сохранить в БД

                            DataBase db = App.getInstance().getDatabase();
                            UserDialogsListDataDAO userDialogsListDataDAO = db.userDialogsListDataDAO();
                            UserDialogsListData.UserData userData = new UserDialogsListData.UserData("null", 0, "null");

                            //userData = UserDialogsListData.UserData.class.getName();



                            singleSubscriber.onSuccess(new UserDialogsListData(data));
                        }
                    });
                } else {
                    //todo достать из БД

                    DataBase db = App.getInstance().getDatabase();
                    UserDialogsListData.UserData userData = new UserDialogsListData.UserData();
                    UserDialogsListDataDAO userDataDAO = db.userDialogsListDataDAO();
                    List<UserDialogsListData.UserData> userDataList = userDataDAO.getAll();


                }
            }
        });
    }

    public Single<DialogData> getMessagesData(final int userId) {
        return Single.create(new Single.OnSubscribe<DialogData>() {

            @Override
            public void call(final SingleSubscriber<? super DialogData> singleSubscriber) {
                final ArrayList<String> inList = new ArrayList<>();
                final ArrayList<String> outList = new ArrayList<>();

                VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, userId));

                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        try {
                            JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                            VKApiMessage[] msg = new VKApiMessage[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                                msg[i] = mes;
                            }

                            for (VKApiMessage mess : msg) {
                                if (mess.out) {
                                    outList.add(mess.body);
                                } else {
                                    inList.add(mess.body);
                                }
                            }
                            DialogData data = new DialogData(inList, outList, userId);
                            singleSubscriber.onSuccess(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Collections.sort(inList);
//                        //Arrays.sort(inList.toArray();
//                        Arrays.sort(outList.toArray());
                    }
                });
            }
        });
    }
}
