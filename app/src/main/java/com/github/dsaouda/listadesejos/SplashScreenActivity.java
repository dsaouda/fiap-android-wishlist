package com.github.dsaouda.listadesejos;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.dsaouda.listadesejos.api.MockyService;
import com.github.dsaouda.listadesejos.callback.LoginCallback;
import com.github.dsaouda.listadesejos.dto.Login;
import com.github.dsaouda.listadesejos.factory.MockyServiceFactory;
import com.github.dsaouda.listadesejos.manager.LoginManager;
import com.github.dsaouda.listadesejos.model.DaoSession;
import com.github.dsaouda.listadesejos.model.LoginDao;
import com.github.dsaouda.listadesejos.repository.LoginRepo;
import retrofit2.Call;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 3000;
    private ImageView ivLogo;

    private final MockyService service = MockyServiceFactory.create();
    private final Call<Login> call = service.login("58b9b1740f0000b614f09d2f");
    private DaoSession daoSession;
    private LoginDao dao;
    private LoginRepo repo;
    private LoginManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        daoSession = ((App) getApplication()).getDaoSession();
        dao = daoSession.getLoginDao();
        repo = new LoginRepo(dao);

        final com.github.dsaouda.listadesejos.model.Login login = repo.defaultLogin();

        if (login != null && login.isManterConectado()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return ;
        }

        manager = new LoginManager(dao, repo);

        ivLogo = (ImageView) findViewById(R.id.ivLogo);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animacao_splash_screen);
        anim.reset();

        if (ivLogo != null) {
            ivLogo.clearAnimation();
            ivLogo.startAnimation(anim);
        }

        if (login == null) {
            final LoginCallback callback = new LoginCallback(manager, this.handler(LoginActivity.class));
            call.enqueue(callback);
        } else {
            SplashScreenActivity.this.handler(login.isManterConectado()
                    ? MainActivity.class
                    : LoginActivity.class).run();
        }
    }

    private Runnable handler(final Class<?> cls) {
        return new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, cls);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        SplashScreenActivity.this.finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }
        };
    }
}