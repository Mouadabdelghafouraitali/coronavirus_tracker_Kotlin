package maa.coronavirustracker_kotlin_vesion.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import kotlinx.android.synthetic.main.bottom_dialog.*
import kotlinx.android.synthetic.main.itemcountry.countryFlag
import kotlinx.android.synthetic.main.itemcountry.countryName
import maa.coronavirustracker_kotlin_vesion.Adapter.CountriesAdapter
import maa.coronavirustracker_kotlin_vesion.Helper
import maa.coronavirustracker_kotlin_vesion.Interfaces.RecyclerViewOnClickListener
import maa.coronavirustracker_kotlin_vesion.Model.Country
import maa.coronavirustracker_kotlin_vesion.R
import maa.coronavirustracker_kotlin_vesion.UI.CoronaViewModel

/**
 * A simple [Fragment] subclass.
 */
class Countries : Fragment() {
    lateinit var root: View
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var countriesAdapter: CountriesAdapter
    lateinit var coronaViewModel: CoronaViewModel
    lateinit var helper: Helper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_countries, container, false)
        initViews(root)
        coronaViewModel = ViewModelProvider.NewInstanceFactory().create(CoronaViewModel::class.java)
        coronaViewModel.getCountriesInformation()
        coronaViewModel.mutableCountriesLiveData.observe(this.viewLifecycleOwner,
            object : Observer<ArrayList<Country>> {
                override fun onChanged(listCountries: ArrayList<Country>?) {
                    progressBar.visibility = View.GONE
                    countriesAdapter = CountriesAdapter(
                        activity!!.applicationContext,
                        listCountries,
                        object : RecyclerViewOnClickListener {
                            override fun onRecyclerViewClickListener(
                                position: Int,
                                country: ArrayList<Country>?
                            ) {
                                showBottomDialog(country, position);

                            }
                        })
                    recyclerView.adapter = countriesAdapter
                }
            })
        return root
    }

    private fun initViews(root: View) {
        helper = Helper()
        progressBar = root.findViewById(R.id.loading)
        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(root.context, 1)
        recyclerView.hasFixedSize()
    }


    fun showBottomDialog(country: ArrayList<Country>?, position: Int) {
        val mBottomSheetDialog = RoundedBottomSheetDialog(root.context)
        val sheetView = LayoutInflater.from(context)
            .inflate(R.layout.bottom_dialog, null)
        mBottomSheetDialog.setContentView(sheetView)
        Glide.with(activity!!.applicationContext)
            .load(country!![position].countryInfo.flag)
            .into(mBottomSheetDialog.countryFlag)
        mBottomSheetDialog.countryName.setText(country[position].country)
        helper.setCustomColor(
            mBottomSheetDialog.Cases,
            "• Cases : ",
            String.format("%,d", (country[position].cases)),
            getResources().getColor(R.color.CasesColor)
        )
        helper.setCustomColor(
            mBottomSheetDialog.TodayCases,
            "• Today cases : ",
            String.format("%,d", (country[position].todayCases)),
            getResources().getColor(R.color.TodayCasesColor)
        )
        helper.setCustomColor(
            mBottomSheetDialog.Deaths,
            "• Deaths : ",
            String.format("%,d", (country[position].deaths)),
            getResources().getColor(R.color.DeathsColor)
        )
        helper.setCustomColor(
            mBottomSheetDialog.TodayDeaths,
            "• Today deaths : ",
            String.format("%,d", (country[position].todayDeaths)),
            getResources().getColor(R.color.TodayDeathsColor)
        )
        helper.setCustomColor(
            mBottomSheetDialog.recovered,
            "• Recovered : ",
            String.format("%,d", (country[position].recovered)),
            getResources().getColor(R.color.RecoveredColor)
        )
        helper.setCustomColor(
            mBottomSheetDialog.Critical,
            "• Critical : ",
            String.format("%,d", (country[position].critical)),
            getResources().getColor(R.color.CriticalColor)
        )
        helper.setCustomColor(
            mBottomSheetDialog.active,
            "• Active : ",
            String.format("%,d", (country[position].active)),
            Color.BLACK
        )
        helper.setCustomColor(
            mBottomSheetDialog.casesPerOneMillion,
            "• Cases/1M : ",
            String.format(
                "%,d",
                (country[position].casesPerOneMillion!!.toInt())
            ), Color.BLUE
        )
        mBottomSheetDialog.show()
    }
}
