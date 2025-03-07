package nader.openchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private String deviceId;
    private RecyclerView recyclerView;

    // Constructor
    public MessageAdapter(List<Message> messageList, String deviceId, RecyclerView recyclerView) {
        this.messageList = messageList;
        this.deviceId = deviceId;
        this.recyclerView = recyclerView;
    }

    // ViewHolder class
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView u_username, u_time;
        public LinearLayout messageContainer;

        public MessageViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.messageTextView); // Make sure these IDs match your layout
            messageContainer = view.findViewById(R.id.messageContainer);
            u_username = view.findViewById(R.id.usernameTextView); // Make sure the ID is correct
            u_time = view.findViewById(R.id.timeTextView); // Ensure unique IDs in layout
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate different layouts based on the type of message
        View view;
        if (viewType == 1) {
            // Sent message (right aligned)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        } else {
            // Received message (left aligned)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (message != null) {
            // Check if message.getDeviceId() is not null and deviceId is not null before using equals
            if (message.getDeviceId() != null && deviceId != null) {
                // Compare senderId with deviceId to align the message layout
                if (message.getDeviceId().equals(deviceId)) {
                    // Sent message, no need to change gravity as it's right-aligned in its layout
                } else {
                    // Received message, no need to change gravity as it's left-aligned in its layout
                }
            } else {
                // Handle cases where message.getSenderId() or deviceId is null
                Log.e("MessageAdapter", "Null senderId or deviceId, cannot compare.");
            }

            // Safely set message text
            String messageText = message.getMessage() != null ? message.getMessage() : "No message content";
            String userName = message.getUsername() != null ? message.getUsername() : "Unknown";
            String timeT = message.getCreatedAt() != null ? message.getCreatedAt() : "time not found";

            holder.messageText.setText(messageText);
            holder.u_username.setText(userName);
            holder.u_time.setText(TimeAgoConv.convert(timeT));
        } else {
            Log.e("MessageAdapter", "Null message at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        // Return 1 for sent messages, 0 for received messages
        Message message = messageList.get(position);
        if (message != null && message.getDeviceId() != null) {
            if (message.getDeviceId().equals(deviceId)) {
                return 1; // Sent message
            }
        }
        return 0; // Received message
    }

    // Method to update the message list
    public void setMessages(List<Message> newMessages) {
        if (messageList != null) {
            messageList.clear();
            messageList.addAll(newMessages);
            notifyDataSetChanged();

            // Smoothly scroll to the bottom after adding the new message
            recyclerView.smoothScrollToPosition(getItemCount() - 1);
        }
    }
}
