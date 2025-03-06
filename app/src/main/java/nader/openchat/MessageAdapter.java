package nader.openchat;

//package nader.openchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nader.openchat.R;
import nader.openchat.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
	
	private List<Message> messages;
	
	public void setMessages(List<Message> messages) {
		this.messages = messages;
		notifyDataSetChanged();
	}
	
	@Override
	public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
		return new MessageViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(MessageViewHolder holder, int position) {
		Message message = messages.get(position);
		holder.messageTextView.setText(message.getText());
	}
	
	@Override
	public int getItemCount() {
		return messages != null ? messages.size() : 0;
	}
	
	public static class MessageViewHolder extends RecyclerView.ViewHolder {
		TextView messageTextView;
		
		public MessageViewHolder(View itemView) {
			super(itemView);
			messageTextView = itemView.findViewById(R.id.messageTextView);
		}
	}
}