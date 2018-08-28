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
import java.util.Calendar;
import java.util.Locale;

import Ivor.Ivor;

// Адаптер, который будет хранить сообщения
public class MessageAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Message> list;

    public MessageAdapter(Context context, ArrayList data) {
        super(context, R.layout.list_msg_layout);
        this.context = context;
        this.list = data;
    }
    public MessageAdapter(Context context, Message[] data) {
        super(context, R.layout.list_msg_layout);
        this.context = context;
        list = new ArrayList();
        for (int i = 0; i < data.length; i++)
            list.add(data[i]);
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
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (message.author != null)
            return createUserMessage(message, inflater, parent);
        else
            return createIvorMessage(message, inflater, parent);
    }

    private View createUserMessage(Message message, LayoutInflater inflater, ViewGroup parent) {
        // Создаем View и наполняем её данными из описанного слоя элемента
        View view = inflater.inflate(R.layout.list_msg_layout, parent, false);
        TextView txt;
        txt =  view.findViewById(R.id.content);
        txt.setText(message.content);
        txt = view.findViewById(R.id.author);
        txt.setText(message.author.realName);
        txt = view.findViewById(R.id.time);

        int ch = Calendar.getInstance().getTime().getHours();
        int min = Calendar.getInstance().getTime().getMinutes();
        txt.setText(String.format(Locale.ENGLISH, "%02d:%02d", ch, min));

        return view;
    }

    private View createIvorMessage(Message message, LayoutInflater inflater, ViewGroup parent) {
        // Создаем View и наполняем её данными из описанного слоя элемента
        View view = inflater.inflate(R.layout.list_msg_layout_ivor, parent, false);
        TextView txt;
        txt =  view.findViewById(R.id.content);
        txt.setText(message.content);

        txt = view.findViewById(R.id.author);
        txt.setText(Ivor.Name);
        txt = view.findViewById(R.id.time);

        int ch = Calendar.getInstance().getTime().getHours();
        int min = Calendar.getInstance().getTime().getMinutes();
        txt.setText(String.format(Locale.ENGLISH, "%02d:%02d", ch, min));

        return view;
    }
}
