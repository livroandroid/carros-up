package br.com.livroandroid.carros.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.CarroActivity
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.extensions.toast
import kotlinx.android.synthetic.main.fragment_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class CarrosFragment : Fragment() {

    private lateinit var tipo: TipoCarro

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_carros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipo = arguments?.getSerializable("tipo") as TipoCarro

        // Init Views
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()

        swipeToRefresh.setOnRefreshListener {
            taskCarros(true)
        }
        swipeToRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_carros, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_about -> {
                toast("About")
                return true
            }
            R.id.action_refresh-> {
                taskCarros(true)
                toast("Refresh")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        taskCarros(false)

    }

    private fun taskCarros(refresh: Boolean) {

        if(! swipeToRefresh.isRefreshing) {
            progress.visibility = View.VISIBLE
        }

        doAsync {

            val carros = CarroService.getCarros(tipo, refresh)

            uiThread {

                recyclerView.adapter = CarroAdapter(carros) { c ->
                    onClickCarro(c)
                }

                swipeToRefresh.isRefreshing = false
                progress.visibility = View.INVISIBLE
            }
        }


        /*doAsync {
            val carros = CarroService.getCarros(tipo)

            uiThread {

                recyclerView.adapter = CarroAdapter(carros) {
                    c -> onClickCarro(c)
                }
            }
        }*/
    }

    private fun onClickCarro(c: Carro) {
        activity?.startActivity<CarroActivity>("carro" to c)
    }
}
