package com.example.instashare.Model;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instashare.R;
import com.example.instashare.Utils.FirebaseUtils;
import com.example.instashare.Utils.InstaShareUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FriendFragment extends Fragment {
    private Uri uri;
    private User user, friend_user;
    private String uid, firstName, lastName, time;
    private Context context;
    private ImageView imgFriend;
    private TextView tvFriendName, tvTimeFriend;
    private ImageView  btn_cry, btn_wow, btn_laughing, btn_love, btn_uparrow, imgbtnreply;
    private LinearLayout lnedtext, lnedtextreply, lnxemanhngkhac;
    private ViewGroup container;
    //private EmojiPopup popup;
    private EditText edtextreply;
    private Button btnbinhluan;
    private ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.page_friend, container, false);
        imgFriend = rootView.findViewById(R.id.imgFriend);
        tvFriendName = rootView.findViewById(R.id.tvFriendName);
        tvTimeFriend = rootView.findViewById(R.id.tvTimeFriend);

        btn_cry = rootView.findViewById(R.id.emoji_cry);
        btn_laughing = rootView.findViewById(R.id.emoji_laughing);
        btn_love = rootView.findViewById(R.id.emoji_love);
        btn_wow = rootView.findViewById(R.id.emoji_wow);
        lnedtext = rootView.findViewById(R.id.lnedtext);
        lnedtextreply = rootView.findViewById(R.id.lnedtextreply);
        lnedtextreply.bringToFront();
        imgbtnreply = rootView.findViewById(R.id.imgbtnreply);
        btnbinhluan = rootView.findViewById(R.id.btn_binhluan);
        edtextreply = rootView.findViewById(R.id.edtextreply);

        Glide.with(context).load(this.uri).into(imgFriend);
        getName();
        tvTimeFriend.setText(InstaShareUtils.getDistanceTime(time));

        btn_wow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji_wow();
            }
        });
        btn_cry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji_cry();
            }
        });
        btn_laughing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji_laughing();
            }
        });
        btn_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji_love();
            }
        });

        btnbinhluan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnedtext.setVisibility(View.GONE);
                lnedtextreply.setVisibility(View.VISIBLE);
                lnedtextreply.requestFocus();
            }
        });
        edtextreply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    edtextreply.clearFocus();
                    lnedtext.setVisibility(View.VISIBLE);
                    lnedtextreply.setVisibility(View.GONE);
                }
                else {

                }
            }
        });
        imgbtnreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtextreply.clearFocus();
                lnedtext.setVisibility(View.VISIBLE);
                lnedtextreply.setVisibility(View.GONE);
                String text = edtextreply.getText().toString();
                if(text.isEmpty() || text.length()< 1){
                    return;
                }
                comment(text);
            }
        });
        edtextreply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do here
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return rootView;
    }

    private void comment(String input){
        String idchatroom = InstaShareUtils.createId(user.getUid(), friend_user.getUid());
        Message newMessage = new Message(input, user.getUid(), Timestamp.now(), uri);
//        FirebaseUtils.Instance().getChats(idchatroom)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            return;
//                        }
//
//                        for (QueryDocumentSnapshot doc : value) {
//                            Message message1= new Message(doc);
//                            if(message1.getUri()==uri){
//                                newMessage.setUri(null);
//                                break;
//                            }
//                        }
//                    }
//                });
        Log.i("search_2", newMessage.toString() );
        if(newMessage!= null)
            FirebaseUtils.Instance().sendMessage(idchatroom, newMessage);
    }
    public FriendFragment(User user, Context context, Uri uri, String uid, String time)
    {
        this.user = user;
        this.context = context;
        this.uri = uri;
        this.uid = uid;
        this.time = time;
    }

    private void getName()
    {
        friend_user = new User();
        friend_user.setUid(uid);
        FirebaseUtils.getName(friend_user, context, new FirebaseUtils.UserNameCallback() {
            @Override
            public void onUserNameLoaded() {
                firstName = friend_user.getFirstName();
                lastName = friend_user.getLastName();
                tvFriendName.setText(firstName + " " + lastName);
            }
        });
    }

    public void dispatchTouchEventFromActivity(MotionEvent event) {
        if (getActivity() != null) {
            getActivity().dispatchTouchEvent(event);
        }
    }

    // Phương thức kiểm tra xem một điểm cụ thể có nằm trong một View không
    private boolean isTouchInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        // Xác định kích thước của view
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();

        // Kiểm tra xem vị trí chạm có nằm trong view không
        return (x >= viewX && x <= (viewX + viewWidth)) && (y >= viewY && y <= (viewY + viewHeight));
    }

    public void flyEmoji(final int resId) {
        ZeroGravityAnimation animation = new ZeroGravityAnimation();
        animation.setCount(1);
        animation.setScalingFactor(2.0f);
        animation.setOriginationDirection(Direction.TOP);
        animation.setDestinationDirection(Direction.BOTTOM);
        animation.setImage(resId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        container = rootView.findViewById(R.id.animation_holder);
        animation.play((Activity) context,container);
    }
    public void emoji_laughing() {
        // You can change the number of emojis that will be flying on screen
        for (int i = 0; i < 15; i++) {
            flyEmoji(R.drawable.emoji_laughing);
        }
    }
    // You can change the number of emojis that will be flying on screen
    public void emoji_love(){
        for(int i=0;i<15;i++) {
            flyEmoji(R.drawable.emoji_love);
        }
    }
    // You can change the number of emojis that will be flying on screen
    public void emoji_wow(){
        for(int i=0;i<15;i++) {
            flyEmoji(R.drawable.emoji_wow);
        }
    }
    public void emoji_cry(){
        for(int i=0;i<15;i++) {
            flyEmoji(R.drawable.emoji_cry);
        }
    }
}
