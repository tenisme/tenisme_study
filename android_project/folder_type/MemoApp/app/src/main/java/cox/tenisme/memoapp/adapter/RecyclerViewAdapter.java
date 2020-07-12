package cox.tenisme.memoapp.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cox.tenisme.memoapp.MainActivity;
import cox.tenisme.memoapp.R;
import cox.tenisme.memoapp.UpdateMemo;
import cox.tenisme.memoapp.data.DatabaseHandler;
import cox.tenisme.memoapp.model.Memo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Memo> memoArrayList;

    Intent i;

    DatabaseHandler dh;
    Memo memo;

    // 생성자 만들기
    public RecyclerViewAdapter(Context context, ArrayList<Memo> memoArrayList){

        this.context = context;
        this.memoArrayList = memoArrayList;
    }

    // 화면 구성
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_row, parent, false);

        return new ViewHolder(view);
    }

    // 리스트(db에 있는 데이터들)에 있는 데이터를 화면에 있는 View 들 위에 표시하는 메소드
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        // 포지션에 맞는 데이터를 리스트에서 꺼내온다
        memo = memoArrayList.get(position);
        // contactList 에 있는 것들의 속성은 Contact 속성이므로 Contact 타입으로 가져온다
        // 가져온 데이터에서 필요한 데이터들을 추출한다
        String getTitle = memo.getTitle();
        String getMemo = memo.getMemo();

        // "뷰홀더"에 있는 텍스트뷰에 문자열을 셋팅한다
        holder.txtTitle.setText(getTitle);
        holder.txtMemo.setText(getMemo);
    }

    // 리스트에 있는 데이터의 갯수를 리턴해줘야 한다.
    @Override
    public int getItemCount() {
        return memoArrayList.size();
    }

    // 하나의 셀(템플릿) xml 화면에 있는 구성 요소(텍스트뷰, 이미지뷰 등등)를 여기서 연결한다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public TextView txtMemo;
        public ImageView imgDelete;

        public CardView cardView;

        RecyclerViewAdapter recyclerViewAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);

            txtMemo = itemView.findViewById(R.id.txtMemo);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            // 카드뷰의 클릭 이벤트 처리
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    i = new Intent(context, UpdateMemo.class);
                    i.putExtra("selectPosition", getAdapterPosition());
                    context.startActivity(i);

                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder completeAlert = new AlertDialog.Builder(context);
                    completeAlert.setTitle("메모 삭제");
                    completeAlert.setMessage("정말 삭제하시겠습니까?");
                    completeAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dh = new DatabaseHandler(context);
                            memoArrayList = dh.getAllMemos();

                            int index = getAdapterPosition();
                            memo = memoArrayList.get(index);

                            dh.deleteMemo(memo);
                            // dh.searchMemo(edit~); 해서 삭제 후 새 데이터 목록이 뜨게하지 말고 검색값이 뜨게 바꿀 것

                            memoArrayList = dh.getAllMemos();
                            recyclerViewAdapter = new RecyclerViewAdapter(context, memoArrayList);

                            notifyDataSetChanged();

                        }
                    });
                    completeAlert.setNegativeButton("No", null); // null > 아무 것도 하지 마라.
                    completeAlert.setCancelable(false);
                    completeAlert.show();
                }
            });

        }
    }
}
