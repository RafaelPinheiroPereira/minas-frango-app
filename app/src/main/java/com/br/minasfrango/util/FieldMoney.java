package com.br.minasfrango.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;

/**
 * Created by 04717299302 on 20/01/2017.
 */

public class FieldMoney
        extends androidx.appcompat.widget.AppCompatEditText
{
    /**
     Indicativo de atualização do campo.
     */
    private boolean
            ib_update;

    /**
     Construtor da classe.
     @param context
     @param attrs
     @param defStyle
     */
    public FieldMoney
    (
            Context context,
            AttributeSet attrs,
            int defStyle
    )
    {
        super
                (
                        context,
                        attrs,
                        defStyle
                );

        //
        // Inicializa o componente de acordo com suas propriedades.
        //
        ComponenteInicializar();
    }

    /**
     Construtor da classe.
     @param context
     @param attrs
     */
    public FieldMoney
    (
            Context context,
            AttributeSet attrs
    )
    {
        super
                (
                        context,
                        attrs
                );

        //
        // Inicializa o componente de acordo com suas propriedades.
        //
        ComponenteInicializar();
    }

    /**
     Construtor da classe.
     @param context
     */
    public FieldMoney
    (
            Context context
    )
    {
        super(context);

        //
        // Inicializa o componente de acordo com suas propriedades.
        //
        ComponenteInicializar();
    }

    /**
     Inicializa o componente.
     */
    private
    void ComponenteInicializar()
    {
        //
        // Adiciona o evento de teclas.
        //
        this.setKeyListener(io_key_listener);

        //
        // Preenche o campo com a mascara a ser utilizada.
        //
        this.setText("VALOR RECEBIDO");
        this.setHint("VALOR RECEBIDO");

        //
        // Seta a seleção na primeira casa.
        //
        this.setSelection(1);

        //
        // Adiciona o listener de mudança no texto.
        //
        this.addTextChangedListener
                (
                        new TextWatcher()
                        {
                            public
                            void afterTextChanged
                                    (
                                            Editable s
                                    )
                            {
                                String
                                        ls_valor_original = s.toString();
//
// Trambique.
//
                                if (ib_update){ib_update = false;return;}
                                //
                                // Se o valor original for menor do que 16 posições.
                                // Obs: (Permite apenas digitar 16 caracteres)
                                //
                                if (
                                        ls_valor_original.length()
                                                < 16
                                        )
                                {
                                    //
                                    // Controlará mascara de edição do campo (casas decimais)
                                    //
                                    StringBuffer
                                            ls_mascara = new StringBuffer();

                                    //
                                    // Adiciona o valor original (contido no campo) com o próximo valor.
                                    // Permitindo apenas caracteres numéricos.
                                    //
                                    ls_mascara.append(ls_valor_original.replaceAll("[^0-9]*",""));

                                    //
                                    // Receberá um número LONG do campo até o momento.
                                    //
                                    Long
                                            ln_number = new Long(ls_mascara.toString());

                                    //
                                    // Faz o replace necessário para manipulação das casas decimais.
                                    //
                                    ls_mascara.replace
                                            (
                                                    0,
                                                    ls_mascara.length(),
                                                    ln_number.toString()
                                            );

                                    //
                                    // Se conteudo menor do que 3.
                                    //
                                    if (
                                            ls_mascara.length() < 3
                                            )
                                    {
                                        //
                                        // Se o tamanho for de 1 posição.
                                        //
                                        if (
                                                ls_mascara.length() == 1
                                                )
                                        {
                                            //
                                            // Insere os caracteres em ordem.
                                            //
                                            ls_mascara.insert(0, "0").insert(0, ",").insert(0, "0");
                                        }

                                        //
                                        // Se o tamanho for de 2 posições.
                                        //
                                        else if (
                                                ls_mascara.length() == 2
                                                )
                                        {
                                            //
                                            // Insere os caracteres em ordem.
                                            //
                                            ls_mascara.insert(0, ",").insert(0, "0");
                                        }
                                    }

                                    //
                                    // Se tiver um tamanho maior que 3.
                                    //
                                    else
                                    {
                                        //
                                        // Insere a virgula.
                                        //
                                        ls_mascara.insert(ls_mascara.length()-2, ",");
                                    }

                                    //
                                    // Se tiver o tamanho de 6 posições.
                                    //
                                    if (
                                            ls_mascara.length() > 6
                                            )
                                    {
                                        //
                                        // Insere o ponto.
                                        //
                                        ls_mascara.insert(ls_mascara.length()-6, '.');

                                        //
                                        // Se o tamanho for maior do que 10.
                                        //
                                        if (
                                                ls_mascara.length() > 10
                                                )
                                        {
                                            //
                                            // Insere o ponto.
                                            //
                                            ls_mascara.insert(ls_mascara.length()-10, '.');

                                            //
                                            // Se for maior do que 14.
                                            //
                                            if (
                                                    ls_mascara.length() > 14
                                                    )
                                            {
                                                //
                                                // insere o ponto.
                                                //
                                                ls_mascara.insert(ls_mascara.length()-14, '.');
                                            }
                                        }
                                    }

                                    //
                                    // Define que o campo esta atualizando.
                                    //
                                    ib_update = true;

                                    //
                                    // Roda o novo valor.
                                    //
                                    FieldMoney.this.setText(ls_mascara);

                                    //
                                    // Faz a seleção.
                                    //
                                    FieldMoney.this.setSelection(ls_mascara.length());
                                }
                            }

                            public
                            void beforeTextChanged
                                    (
                                            CharSequence s,
                                            int start,
                                            int count,
                                            int after
                                    )
                            {

                            }

                            public
                            void onTextChanged
                                    (
                                            CharSequence s,
                                            int start,
                                            int before,
                                            int count
                                    )
                            {

                            }
                        }
                );
    }

    private
    final KeylistenerNumber
            io_key_listener = new KeylistenerNumber();

    private
    class KeylistenerNumber
            extends NumberKeyListener
    {

        public
        int getInputType()
        {
            return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

        }

        @Override
        protected
        char[] getAcceptedChars()
        {
            return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        }
    }
}
