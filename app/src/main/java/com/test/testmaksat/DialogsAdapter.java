package com.test.testmaksat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.test.testmaksat.data.UserDialogsListData;

public class DialogsAdapter extends BaseAdapter {

    private UserDialogsListData dialogsData;
    private OnDialogClickListener clickListener;

    public DialogsAdapter(UserDialogsListData dialogsData, OnDialogClickListener clickListener) {
        this.dialogsData = dialogsData;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return dialogsData.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        DialogAdapter.SetData setData = new DialogAdapter.SetData();
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.style_listview, null);

        setData.user_name = (TextView) view.findViewById(R.id.user_name);
        setData.msg = (TextView) view.findViewById(R.id.msg);

        final UserDialogsListData.UserData data = dialogsData.getList().get(position);
        setData.user_name.setText(data.getUserName());
        setData.msg.setText(data.getLastMessage());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onDialogClick(data.getUserId());
            }
        });
        return view;
    }

    public static class SetData {
        TextView user_name, msg;
    }

    interface OnDialogClickListener {

        void onDialogClick(int userId);
    }
}