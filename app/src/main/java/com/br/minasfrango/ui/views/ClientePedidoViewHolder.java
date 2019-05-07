package com.br.minasfrango.ui.views;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.realm.PedidoORM;

public class ClientePedidoViewHolder extends ParentViewHolder   {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private  ImageView mArrowExpandImageView;
    private TextView mFantasia,enderecoTextView;



    public ClientePedidoViewHolder(View itemView) {
        super(itemView);
        mFantasia = itemView.findViewById(R.id.textViewNomeFantasia);
        mArrowExpandImageView = itemView.findViewById(R.id.imgExpand);
        enderecoTextView= itemView.findViewById(R.id.textViewEndereco);
    }

    public void bind(Cliente cliente) {
        mFantasia.setText(cliente.getNome());
        enderecoTextView.setText(cliente.getEndereco());
    }

    public PedidoORM mPedido() {
        return new PedidoORM();
    }
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if (expanded) {
            mArrowExpandImageView.setRotation(ROTATED_POSITION);

        } else {
            mArrowExpandImageView.setRotation(INITIAL_POSITION);
        }

    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        mArrowExpandImageView.startAnimation(rotateAnimation);

    }
}
