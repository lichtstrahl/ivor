package root.ivatio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ivor.Ivor;

// Адаптер, который будет хранить сообщения
public class MessageAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private List<Message> list;

    public MessageAdapter(Context context, List<Message> data) {
        super(context, R.layout.list_msg_layout);
        this.inflater = LayoutInflater.from(context);
        this.list = data;
    }
    public MessageAdapter(Context context, Message[] data) {
        super(context, R.layout.list_msg_layout);
        this.inflater = LayoutInflater.from(context);
        list = new ArrayList(Arrays.asList(data));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Message getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = list.get(position);
        if (message.author != null)
            return createUserMessage(message, parent);
        else
            return createIvorMessage(message, parent);
    }

    private View createUserMessage(Message message, ViewGroup parent) {
        // Создаем View и наполняем её данными из описанного слоя элемента
        View view = inflater.inflate(R.layout.list_msg_layout, parent, false);
        TextView txt;
        txt =  view.findViewById(R.id.content);
        txt.setText(message.content);
        txt = view.findViewById(R.id.author);
        txt.setText(message.author.realName);
        txt = view.findViewById(R.id.time);

        int ch = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        txt.setText(String.format(Locale.ENGLISH, "%02d:%02d", ch, min));
        return view;
    }

    private View createIvorMessage(Message message, ViewGroup parent) {
        // Создаем View и наполняем её данными из описанного слоя элемента
        View view = inflater.inflate(R.layout.list_msg_layout_ivor, parent, false);
        TextView txt;
        txt =  view.findViewById(R.id.content);
        txt.setText(message.content);

        txt = view.findViewById(R.id.author);
        txt.setText(Ivor.getName());
        txt = view.findViewById(R.id.time);

        int ch = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        txt.setText(String.format(Locale.ENGLISH, "%02d:%02d", ch, min));

        return view;
    }

    public Message getLast() {
        return list.isEmpty() ? null : list.get(list.size()-1);
    }

    public void append(Message message) {
        if (message != null) {
            list.add(message);
            notifyDataSetChanged();
        }
    }
    public void append(List<Message> messages) {
        if (messages != null) {
            for (Message m : messages)
                append(m);
        }
    }

    @Override
    public void clear() {
        list.clear();
        super.clear();
    }
}
