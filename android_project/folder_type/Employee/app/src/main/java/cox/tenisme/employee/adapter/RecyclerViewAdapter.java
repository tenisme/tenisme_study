package cox.tenisme.employee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cox.tenisme.employee.R;
import cox.tenisme.employee.model.Employee;

// 4. 1에서 만든 클래스를 상속 처리
    // extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    // 여기까지 쓰면 빨간 줄 생기는 게 맞음.
        // Alt+Enter > implement method > 이 클래스에서 오버라이딩할 메소드들을 선택(전부 선택)하고, OK를 눌러 메소드 생성
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    // 5. 멤버변수 만들기
        // Context 와 ArrayList<> 멤버변수 만들기
    Context context;
    ArrayList<Employee> employeeArrayList;
        // 리사이클러뷰를 쓴다는 것은, 데이터를 여러개를 처리한다는 것. ArrayList<> 는 반드시 필요하다.

    // 6. 위의 두 멤버변수에 데이터를 저장해주는 기능을 하는 생성자 만들기
        // Alt+Insert > Constructor > 위에 만들어놓은 멤버변수 두 개를 전부 선택 후 OK를 눌러 생성자 만들기
    public RecyclerViewAdapter(Context context, ArrayList<Employee> employeeArrayList) {
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

    // 7. 오버라이딩 함수 구현
        // 선행 - 아래 함수의 리턴값 변경 : 상단 public 옆 ViewHolder(클래스 타입, 리턴값)을
            // extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>의 RecyclerViewAdapter.ViewHolder 를
            // 복붙해서 바꿔준다.
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 7-1. 이하 고정 템플릿. 이렇게 써야 부모 뷰와 "리스트 양식 레이아웃 xml" 이 연결됨.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row,parent,false);
        // 7-2. return 옆의 null 을 지우고 new ViewHolder(view); 입력
        return new ViewHolder(view);

    }

    // 8. 파라미터값 변경 : 하단 onBindViewHolder(@NonNull ViewHolder holder, int position)에서
        // ViewHolder 부분을 역시 RecyclerViewAdapter.ViewHolder 로 바꿔준다.
        // Bind > 묶는다는 뜻.
            // onBindViewHolder(){} : 어레이리스트와 xml 을 묶어서 화면과 연결하는 곳
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        // 8-1. ArrayList 에 저장된 데이터들을 화면(xml)과 연결하기 : bind(묶기)
            // 8-1-1. 연결을 위해 ArrayList 에서 position 파라미터를 통해 하나씩 뽑아낸 데이터를
                // 데이터 저장/관리 클래스(여기서는 Employee) 형식으로 가져와서 저장한다
        Employee employee = employeeArrayList.get(position);
        // position 파라미터를 통해 ArrayList 에 저장된 순서대로
            // 가져와야 하는(리스트 레이아웃 템플릿에 배치해야 하는) 모든 데이터를 꺼낸다
        String name = employee.getName();
        int age = employee.getAge();
        int salary = employee.getSalary();
        // 꺼낸 데이터를 xml 에 있는 뷰들과 연결한다
        holder.txtName.setText(name);
        holder.txtAge.setText(age+"세"); // ★String 이외의 자료형은 ""+를 붙여서 String 타입으로 변경시켜주기.
        holder.txtSalary.setText(""+salary);
        // 뷰와 어레이리스트 연결 끝
    }

    // 9. 아이템(데이터)갯수 리턴하는 메소드 구현
    @Override
    public int getItemCount() {
        return employeeArrayList.size(); // ★size() 기억!!
    }

    // 10. RecyclerView 가 들어있는 액티비티로 돌아가서 RecyclerView 를 최종 셋팅한다

    // 0. 먼저, RecyclerView 를 사용할 액티비티의 .xml 에서 RecyclerView 를 추가/아이디설정하고
        // MainActivity.java 에서 RecyclerView 를 선언해서 클래스를 등록한다
    // 1. 뷰홀더 클래스를 만든다.
        // 1-1. public class ViewHolder extends RecyclerView.ViewHolder{} 쓰고 빨간색 뜨면 Alt+Enter
    public class ViewHolder extends RecyclerView.ViewHolder{

        // 2. employee_row.xml 의 뷰를 가져오기
        public TextView txtName;
        public TextView txtAge;
        public TextView txtSalary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 3. 가져온 뷰를 연결하기
            txtName = itemView.findViewById(R.id.txtName);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtSalary = itemView.findViewById(R.id.txtSalary);

        }
    }
}
