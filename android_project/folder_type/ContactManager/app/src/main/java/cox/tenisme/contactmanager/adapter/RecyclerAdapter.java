package cox.tenisme.contactmanager.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cox.tenisme.contactmanager.MainActivity;
import cox.tenisme.contactmanager.UpdateContact;
import cox.tenisme.contactmanager.R;
import cox.tenisme.contactmanager.data.DatabaseHandler;
import cox.tenisme.contactmanager.model.Contact;

// 마지막으로, 이 어댑터 클래스에 아까 만들었던 뷰홀더 클래스를 연결한다.
    // RecyclerView.Adapter 옆에 <RecyclerAdapter.ViewHolder>를 붙여서 연결한다.
// 이제 RecyclerAdapter.ViewHolder 를 복사해서 RecyclerView.ViewHolder 에 붙여넣어 바꿔준다.
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    // 멤버변수 셋팅
    Context context;
    ArrayList<Contact> contactList;

    Intent i;

    DatabaseHandler dh;
    Contact contact;

    // 생성자 만들기 - set 용도
    public RecyclerAdapter(Context context, ArrayList<Contact> contactList){
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 화면을 구성하라

        // 첫번째 파라미터인, parent 로부터 뷰를 생성한다.
            // contact_row 를 가지고 생성해라 라는 명령문
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        // 뷰홀더에 이 view 를 넘겨라(리턴해라). (넘겨서 이 view 의 행동 등을 셋팅해라)
            // new 는 객체생성!! new 옆은 생성자!!!
        return new ViewHolder(view);

    }

    // 리스트(db에 있는 데이터들)에 있는 데이터를 "화면에 있는 View 들 위에 표시하는" 메소드
        // int position : "★화면에 표시되는★ 리스트"의 "인덱스" 번호
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        // 포지션에 맞는 데이터를 리스트에서 꺼내온다
        contact = contactList.get(position);
            // contactList 에 있는 것들의 속성은 Contact 속성이므로 Contact 타입으로 가져온다
        // 가져온 데이터에서 필요한 데이터들을 추출한다
        String name = contact.getName();
        String phone = contact.getPhoneNumber();

        // "뷰홀더"에 있는 "텍스트뷰"에 문자열을 셋팅해라(표시해라!!!!!!!!!!)
        holder.txtName.setText(name);
        holder.txtPhone.setText(phone);
    }

    // 리스트에 있는 데이터의 갯수를 리턴해줘야 한다.
    @Override
    public int getItemCount() {
        return contactList.size(); // .size() : 어레이리스트 안에 있는 데이터의 개수를 리턴해주는 함수
    }

    // 하나의 셀(템플릿) xml 화면에 있는 구성 요소(텍스트뷰, 이미지뷰 등등)를 여기서 연결한다.
        // 액티비티 자바 파일 설정해주듯이 해주면 됨.
        // 이 ViewHolder 는 화면을 구성하는 RecyclerView.ViewHolder 를 상속받는다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName;
        public TextView txtPhone;
        public ImageView imgDelete;
        // 카드뷰를 클릭하면 화면이 넘어갈 수 있도록 1. 멤버변수를 셋팅한다
        public CardView cardView;

        RecyclerAdapter recyclerAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 1. 생성자 안에서 멤버변수를 연결
                // 액티비티에서 하는 연결과 다르므로 다른 연결 방식을 쓴다
            txtName = itemView.findViewById(R.id.txtName);
                // itemView 는 위에서 보내준 변수(view)를 받을 것(return new ViewHolder(view);)이고,
                // 이 view 는 여기서 R.layout.contact_row 를 의미하므로,
                // contact_row.xml 에 있는 id룰 찾아 가져오려면 itemView.findViewById(); 라고 써야한다.
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            // 카드뷰의 클릭 이벤트 처리
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 수정하는 액티비티로 넘어가는 코드 작성
                    i = new Intent(context, UpdateContact.class);
                    i.putExtra("selectPosition", getAdapterPosition());
                    context.startActivity(i);

//                    // 잘 연결되었는지 토스트 띄우기
//                    // context -> 전역변수임. 이 파일 맨 위에 보면 생성자에 context 받아오라고 되어있음.
//                        // 여기에 MainActivity.this 를 연결해주면 아래의 context 에 MainActivity.this 가 연결된다.
//                    Toast.makeText(context, "이 셀은 "+getAdapterPosition()+"번째 셀입니다.", Toast.LENGTH_SHORT).show();
//                    // ★ getAdapterPosition() -> 현재 내가 클릭한 부분이 몇번째 셀인지 알려주는 함수. ★
//                        // 어댑터 클래스에서만 쓸 수 있다. 다른 속성의 클래스에서 getAdapterPosition()을 쓰려면,
//                        // 이 클래스와 다른 클래스가 Intent 클래스의 putExtra()와 get~Extra()로 정보를 주고받아야 한다.
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder completeAlert = new AlertDialog.Builder(context);
                    completeAlert.setTitle("연락처 삭제");
                    completeAlert.setMessage("정말 삭제하시겠습니까?");
                    completeAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dh = new DatabaseHandler(context);
                            contactList = dh.getAllContacts();

                            int index = getAdapterPosition();
                            contact = contactList.get(index);

                            dh.deleteContact(contact);

                            // DB가 삭제된 화면으로 갱신 하기 (1번째 방법)
                            // 바뀐 데이터셋을 다시 가져오고, 바뀐 목록을 어댑터에 다시 추가한다.
                            contactList = dh.getAllContacts();
                            recyclerAdapter = new RecyclerAdapter(context, contactList);
                            // 어댑터한테 데이터셋이 바뀌었음을 알려주는 메소드를 실행한다.
                            // 어댑터는 이것을 감지하고 화면에 다시 새 리스트를 뿌린다.
                            notifyDataSetChanged();
                            // 이 1번째 방법이 현업에서 제일 많이 쓰는 방법이다.

                        }
                    });
                    completeAlert.setNegativeButton("No", null);
                        // 실행시 아무 것도 안 하겠다
                    completeAlert.setCancelable(false);
                    completeAlert.show();
                    // AlertDialog 는
                }
            });

        }
    }
}
