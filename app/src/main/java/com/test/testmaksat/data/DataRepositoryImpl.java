package com.test.testmaksat.data;


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

public class DataRepositoryImpl {

    public Single<UserDialogsListData> getUsersData() {
        return Single.create(new Single.OnSubscribe<UserDialogsListData>() {
            @Override
            public void call(final SingleSubscriber<? super UserDialogsListData> singleSubscriber) {
                //todo найти проверку,есть ли интернет
                if (true) {
                    final VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 50));
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
                            singleSubscriber.onSuccess(new UserDialogsListData(data));
                        }
                    });
                } else {
                    //todo достать из БД
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
