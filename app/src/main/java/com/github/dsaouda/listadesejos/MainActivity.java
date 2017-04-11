package com.github.dsaouda.listadesejos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.dsaouda.listadesejos.model.DaoSession;
import com.github.dsaouda.listadesejos.model.Login;
import com.github.dsaouda.listadesejos.model.LoginDao;
import com.github.dsaouda.listadesejos.model.Produto;
import com.github.dsaouda.listadesejos.model.ProdutoDao;
import com.github.dsaouda.listadesejos.repository.LoginRepo;
import com.github.dsaouda.listadesejos.repository.ProdutoRepo;
import com.github.dsaouda.listadesejos.view.adapter.ProdutoListaAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DaoSession daoSession;
    private ProdutoDao dao;
    private ProdutoRepo repo;

    @BindView(R.id.rvProdutoLista)
    RecyclerView rvProdutoLista;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_LONG).show();
                break;

            case 201:
                Toast.makeText(MainActivity.this, "Produto salvo com sucesso", Toast.LENGTH_LONG).show();
                loadRecycleViewEnderecoLista();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            final LoginDao loginDao = daoSession.getLoginDao();
            final LoginRepo loginRepo = new LoginRepo(loginDao);
            final Login login = loginRepo.defaultLogin();

            login.setManterConectado(false);
            loginDao.insertOrReplace(login);

            finish();
            System.exit(0);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_produto) {
            // Handle the camera action
        } else if (id == R.id.nav_login) {

        } else if (id == R.id.nav_splashscreen) {

        } else if (id == R.id.nav_sobre) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        ButterKnife.bind(this);
        rvProdutoLista.setLayoutManager(new LinearLayoutManager(this));

        daoSession = ((App) getApplication()).getDaoSession();
        dao = daoSession.getProdutoDao();
        repo = new ProdutoRepo(dao);

        loadRecycleViewEnderecoLista();
        loadToolbar();
        loadFabButton();
        loadToogle();
        loadNavigationView();
    }

    private void loadNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadToogle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void loadToolbar() {
        setSupportActionBar(toolbar);
    }

    private void loadFabButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, ProdutoActivity.class);
                startActivityForResult(intent, 200);
            }

        });
    }

    private void loadRecycleViewEnderecoLista() {
        final List<Produto> produtos = repo.loadAll();
        final ProdutoListaAdapter adapter = new ProdutoListaAdapter(produtos, this, dao);

        findViewById(R.id.tvSemProduto)
                .setVisibility(produtos.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        rvProdutoLista.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*
        //swipe
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Snackbar.make(viewHolder.itemView, "Deletado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                ((EnderecoViewHolder)viewHolder).removerEndereco();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvEnderecoLista);
        */

    }
}
