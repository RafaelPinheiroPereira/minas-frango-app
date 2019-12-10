package com.br.minasfrango.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.br.minasfrango.R;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.mvp.exclusao.IExclusaoMVP;
import com.br.minasfrango.ui.mvp.exclusao.Presenter;
import com.br.minasfrango.util.DateUtils;
import java.util.Date;

 public class ExclusaoActivity extends AppCompatActivity implements IExclusaoMVP.IView {

    IExclusaoMVP.IPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.data_inicial_calendar_view)
    CalendarView dataInicialCalendarView;

    @BindView(R.id.data_final_calendar_view)
    CalendarView dataFinalCalendarView;

    @BindView(R.id.btnExcluir)
    Button btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusao);
        ButterKnife.bind(this);
        initViews();
    }

    @OnClick(R.id.btnExcluir)
    public void excluirPedido() {
        if (DateUtils.ehUmPeriodoValido(mPresenter.getDataInicial(), mPresenter.getDataFinal())) {
            mPresenter.excluirPedido();
            mPresenter.exibirMensagemSucesso();
            this.finish();
        } else {
           mPresenter.exibirMensagemErro();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = new Presenter(this);
        mPresenter.setDataInicial(new Date(dataInicialCalendarView.getDate()));
        mPresenter.setDataFinal(new Date(dataFinalCalendarView.getDate()));
        dataInicialCalendarView.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(
                            CalendarView view, int year, int month, int dayOfMonth) {
                        // display the selected date by using a toast
                        mPresenter.setDataInicial(new Date(view.getDate()));
                    }
                });
        dataFinalCalendarView.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(
                            CalendarView view, int year, int month, int dayOfMonth) {
                        // display the selected date by using a toast
                        mPresenter.setDataFinal(new Date(view.getDate()));
                    }
                });
    }

    @Override
    public void exibirMensagemSucesso() {
        AbstractActivity.showToast(this, "Dados Excluídos com Sucesso!");
    }

     @Override
     public void exibirMensagemErro() {
         AbstractActivity.showToast(this, "Data Inicial dever ser menor ou igual a Data Final!");

     }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
