package root.ivatio;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import root.ivatio.ivor.Ivor;

// Адаптер, который будет хранить сообщения
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private LayoutInflater inflater;
    private List<Message> list;
    private Resources res;
    private static final int IVOR_MSG = 1;
    private static final int USER_MSG = 2;

    public MessageAdapter(Context context, List<Message> data) {
        this.inflater = LayoutInflater.from(context);
        this.list = data;
        res = context.getResources();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessageViewHolder(inflater.inflate(R.layout.list_msg_layout, viewGroup, false));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).author == null ? IVOR_MSG : USER_MSG;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int i) {
        switch (getItemViewType(i)) {
            case IVOR_MSG:
                viewHolder.bindIvorMessageView(i);
                break;
            case USER_MSG:
                viewHolder.bindUserMessageView(i);
                break;
            default:
        }
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

    public void clear() {
        list.clear();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView viewContent;
        private final TextView viewAuthor;
        private final TextView viewTime;
        private final ViewGroup layoutBG;
        private final ViewGroup layoutTime;
        private final LinearLayout.LayoutParams paramLeft;
        private final LinearLayout.LayoutParams paramRight;


        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContent = itemView.findViewById(R.id.content);
            viewAuthor = itemView.findViewById(R.id.author);
            viewTime = itemView.findViewById(R.id.time);
            layoutBG = itemView.findViewById(R.id.layoutBG);
            layoutTime = itemView.findViewById(R.id.timeLayout);
            paramLeft = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramLeft.gravity = Gravity.START;
            paramRight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramRight.gravity = Gravity.END;
        }

        void bindUserMessageView(int pos) {
            layoutBG.setBackgroundColor(res.getColor(R.color.msgBg));

            Message msg = list.get(pos);
            int ch = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int min = Calendar.getInstance().get(Calendar.MINUTE);
            viewContent.setText(msg.content);
            viewAuthor.setText(msg.author == null ? Ivor.getName() : msg.author.realName);
            viewTime.setText(String.format(Locale.ENGLISH, "%02d:%02d", ch, min));
        }

        void bindIvorMessageView(int pos) {
            layoutBG.setBackgroundColor(res.getColor(R.color.msgIvorBg));
            viewContent.setLayoutParams(paramLeft);
            layoutTime.setLayoutParams(paramRight);
            Message msg = list.get(pos);
            int ch = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int min = Calendar.getInstance().get(Calendar.MINUTE);
            viewContent.setText(msg.content);
            viewAuthor.setText(msg.author == null ? Ivor.getName() : msg.author.realName);
            viewTime.setText(String.format(Locale.ENGLISH, "%02d:%02d", ch, min));
        }
    }
}
