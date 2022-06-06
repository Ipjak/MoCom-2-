package NB.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import NB.data.BoardItem;
import NB.data.REF;
import NB.data.MyUser;
import NB.fragment.ChatFragment;
import NB.fragment.HomeFragment;
import NB.fragment.NearFragment;

public class BoardActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    NearFragment nearFragment;
    ChatFragment chatFragment;

    FloatingActionButton addButton;

    private DatabaseReference myRef;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //액션바 보이기
        setTheme(R.style.Theme_NeedBlood);
        getSupportActionBar().setTitle(R.string.board);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        myRef = FirebaseDatabase.getInstance().getReference(REF.LIST.name());

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        BoardItem boardItem = data.getParcelableExtra(REF.LIST.toString());
                        myRef.push().setValue(boardItem);
                    }
                });

        //계정정보
        Intent userIntent = getIntent();
        MyUser user = userIntent.getParcelableExtra(REF.USER.name());
        Toast.makeText(this, user.userName+getString(R.string.signin_complete), Toast.LENGTH_SHORT).show();

        /*프래그먼트*/
        homeFragment = new HomeFragment();
        nearFragment = new NearFragment();
        chatFragment = new ChatFragment();

        addButton = findViewById(R.id.home_add_button);

        /*게시판 글쓰기 버튼*/
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, WriteActivity.class);
            intent.putExtra(REF.USER.name(), user);
            launcher.launch(intent);
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    addButton.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle(R.string.board);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                case R.id.nearMap:
                    addButton.setVisibility(View.INVISIBLE);
                    getSupportActionBar().setTitle(R.string.map);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, nearFragment).commit();
                    return true;
                case R.id.chatting:
                    addButton.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
                    return true;
                case R.id.setting:
                    //설정 화면 시작
                    Toast.makeText(getApplicationContext(), "Set", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        });
    }

}