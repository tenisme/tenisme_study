package cox.tenisme.contactmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import cox.tenisme.contactmanager.adapter.RecyclerAdapter;
import cox.tenisme.contactmanager.data.DatabaseHandler;
import cox.tenisme.contactmanager.model.Contact;

public class MainActivity extends AppCompatActivity {

    Button btnAddConTact;

    RecyclerView recyclerView; // 메인액티비티와 연결된 xml 에서 가져온 뷰

    RecyclerAdapter recyclerAdapter; // 하나의 셀을 연결시키는 어댑터
    ArrayList<Contact> contactArrayList; // 데이터베이스에서 읽어온 주소록 정보를 저장할 리스트
    // 리스트 항목 뷰를 처리할 때에는 리사이클러 뷰, 셀 연결(뷰 처리) 어댑터, 데이터베이스를 담은 ArrayList 세 개가 꼭 필요하다.

    DatabaseHandler dh;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddConTact = findViewById(R.id.btnAddContact);
        // 1. 리사이클러 뷰를 연결하고 기본적인 셋팅을 한다
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true); // 모든 셀마다 고정 사이즈로 하라
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this)); // 한 라인에 한 셀만 들어갈 것이므로 LinearLayout 을 쓴다. xml과 연결된 레이아웃을 쓰는 게 아님.

        // 이하 2, 3번 리사이클러뷰 화면 구현 짝궁^.^
        // 2, 3번 이후 화면을 갱신해야 할 때는 밑에 recyclerAdapter.notifyDataSetChanged();만 붙여주면 된다.
        // ★ 2. 데이터베이스에서 테이블에 저장된 데이터를 읽어서 어레이리스트에 저장
        dh = new DatabaseHandler(MainActivity.this);
        contactArrayList = dh.getAllContacts();

        // ★ 3. 아까 만든 (하나의 셀을 표시하는) 어댑터를 생성해서, 리사이클러뷰에 연결시킨다
        recyclerAdapter = new RecyclerAdapter(MainActivity.this, contactArrayList);
        recyclerView.setAdapter(recyclerAdapter);
        // 2, 3번만 있으면 리스트 화면이 구현된다.

        btnAddConTact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(i, 0);
            }
        });


//        // 1. DatabaseHandler 를 사용하기 위해서 가져오기(테이블 만들기) ★
//        dh = new DatabaseHandler(MainActivity.this);

//        // 2. Contact 로 새 데이터를 만들거나 불러온다.
//            // 데이터를 insert 할 때(만들 때)만 new Contact()이고,
//                // Contact new_contact = new Contact();
//            // update 랑 delete 는 기존에 저장된 곳에서 값을 가져와야 하기 때문에 new ~가 아닌 핸들러의 .getContact() 함수와 이어준다.
//                // Contact contact = dh.getContact(1);

//        // 3. 업데이트/딜리트시 데이터 잘 가져왔는지 로그 찍어보기
//        Log.i("MyDB", "아이디 "+contact.getId()+"번 데이터 : "+"이름 - "+contact.getName()+
//                ",  전화번호 - "+contact.getPhoneNumber());

//        // 4-1. 인서트
//        // 데이터 하나를 만들어서 DB(테이블)에 저장하기
//        new_contact.setName("Jeremy");
//        new_contact.setPhoneNumber("010-1234-5678");
//        // addContact(); 메소드로 db에 실제로 저장한다.
//          // addContact();에 Contact 타입(데이터)이 필요하므로 새 Contact 를 먼저 선언해준 것이다. 업데이트와 딜리트도 마찬가지.
//        dh.addContact(new_contact);
//        // 아무 제어도 없는 현재 상태로는 앱을 실행할 때마다 위 데이터가 계속 추가된다

//        // 4-2. 업데이트
//        // 데이터 정보 바꾸기
//        contact.setName("yap~!");
//        // 업데이트 메소드 실행
//        dh.updateContact(contact);

//        // 4-3. 딜리트
//        // 딜리트 메소드 실행
//        dh.deleteContact(contact);

//        // 디비 테이블에 저장된 데이터 갯수 가져오기 count(*)
//        int count = dh.getCount();
//        Log.i("MyDB", "contacts 테이블에 저장된 데이터 갯수 : "+count);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){

        }
    }

    // ★ 다음 액티비티에서 finish()(onStop())되어서 돌아올 때 실행하는 것(및 시작할 때 실행할 것)은 onResume()에 추가한다. ★
        // onStart()는 잘 안 쓴다고 한다.
        // 깃헙 29번 액티비티 라이프사이클 참조
    @Override
    protected void onResume() {
        super.onResume();
        // 리스트 화면 구현 2, 3번 복붙
        dh = new DatabaseHandler(MainActivity.this);
        contactArrayList = dh.getAllContacts();

        // 어댑터를 셋팅하고(어댑터 클래스 인스턴스를 만들고)
        recyclerAdapter = new RecyclerAdapter(MainActivity.this, contactArrayList);
        // 이 "어댑터"를 리사이클러 뷰에 "셋팅"해준다 > setAdapter();
        recyclerView.setAdapter(recyclerAdapter);

//        // 저장 확인용 로그를 만드는 과정
//        // 여기(onResume)에서 DatabaseHandler 를 사용한다.
//        // 데이터베이스 핸들러 클래스를 객체 생성한다. => contacts 테이블이 생성된다
//        dh = new DatabaseHandler(MainActivity.this);
//        // 저장된 데이터를 모두 다 가져와서 읽어오는 코드(확인용 로그를 위해 만듦)
//        // 1. 모든 Contact 정보를 담는 ArrayList 타입 참조변수를 생성
//        ArrayList<Contact> contactList = dh.getAllContacts();
//        // 2. for 문으로 contactList 의 모든 정보를 하나씩 전부 불러온다
//        for(Contact contact : contactList){
//            // Contact contact : contactList -> contact 의 시작부터 끝까지(모두 다) 가져와라 하는 for 문 문법
//            Log.i("MyDB", "저장된 주소록의 데이터 id : "+contact.getId()+", 이름은 : "
//                    +contact.getName()+", 전화번호는 : "+contact.getPhoneNumber());
//        }
    }

//    // DB가 삭제된 화면으로 갱신 하기 (2번째 방법)
//        // 1번째 방법은 java > 1번째 패키지 > adapter > RecyclerAdapter.java 에 있음
//    // 데이터베이스 정보를 가져와서 화면을 갱신한다 - 메인 액티비티에서 refresh 함수를 직접 만드는 방법
//    public void refresh(){
//
//        dh = new DatabaseHandler(MainActivity.this);
//        contactArrayList = dh.getAllContacts();
//
//        recyclerAdapter = new RecyclerAdapter(MainActivity.this, contactArrayList);
//        recyclerView.setAdapter(recyclerAdapter);
//
//        recyclerAdapter.notifyDataSetChanged();
//    }
//    // 이걸 다른 액티비티에서 쓰려면(다른 자바 파일에서 화면을 갱신하려면) 이렇게 쓰면 된다
//    // ((MainActivity)context).refresh();

    // DB가 삭제된 화면으로 갱신 하기 (3번째 방법)
    // Intent 로 데이터 주고받기 - Main 으로 돌아올 때 2번째 함수 안의 코드를 onActivityResult() 안에 적어서 갱신한다
}