package com.github.dsaouda.wishlist.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dsaouda.wishlist.ProdutoActivity;
import com.github.dsaouda.wishlist.R;
import com.github.dsaouda.wishlist.model.Produto;
import com.github.dsaouda.wishlist.model.ProdutoDao;
import com.github.dsaouda.wishlist.view.adapter.ProdutoListaAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProdutoViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    @BindView(R.id.ivItemProduto)
    ImageView ivItemProduto;

    @BindView(R.id.tvProduto)
    TextView tvProduto;

    @BindView(R.id.tvValor)
    TextView tvValor;

    @BindView(R.id.tvTag)
    TextView tvTag;

    private final ProdutoDao dao;
    private final ProdutoListaAdapter adapter;
    private Produto produto;
    private View view;

    public ProdutoViewHolder(final View view, final ProdutoListaAdapter adapter, final ProdutoDao dao) {
        super(view);

        ButterKnife.bind(this, view);

        this.adapter = adapter;
        this.dao = dao;
        this.view = view;
        view.setOnLongClickListener(this);
    }

    public void removerEndereco() {
        dao.delete(produto);
        adapter.remove(getAdapterPosition());
    }

    public void preencher(Produto produto) {
        this.produto = produto;

        Picasso.with(view.getContext())
                .load(produto.getImage())
                .noFade()
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.drawable.ic_menu_gallery)
                .fit()
                .centerCrop()
                .into(ivItemProduto);

        tvProduto.setText(produto.getNome());
        tvValor.setText("R$ " + String.valueOf(produto.getValor()));
        tvTag.setText(produto.getTag());
    }


    @Override
    public boolean onLongClick(final View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_item_produto, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.menuEditar:

                        final Activity context = (Activity)v.getContext();
                        final Intent intent = new Intent(context, ProdutoActivity.class);
                        intent.putExtra("produto", produto.getId());
                        context.startActivityForResult(intent, 201);

                        break;

                    case R.id.menuDeletar:
                        removerEndereco();

                        break;

                    case R.id.menuVisualizarExterno:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(produto.getUrl()));
                        view.getContext().startActivity(browserIntent);
                        break;
                }

                return true;
            }
        });

        popup.show();
        return false;
    }

}