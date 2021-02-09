package com.example.myfundkt.ui.fragment.viewPage2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myfundkt.R
import com.example.myfundkt.utils.MyLog
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_pages.*
import kotlinx.android.synthetic.main.fragment_pages.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val TAG = "PagesFragment"
class PagesFragment : Fragment() {
    val pages = intArrayOf(R.id.fundInfoFragment,R.id.holdingStocksFragment,R.id.editFragment)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyLog.d("PagesFragment","onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pages, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller: NavController? = activity?.let { Navigation.findNavController(it, R.id.fragment) }
        val graph = controller ?.graph
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onViewCreated:selectedTabPosition "+tabLayout.selectedTabPosition)
                graph?.let {
                    it.startDestination = pages[tabLayout.selectedTabPosition]
                    controller.graph = it
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        Log.d(TAG, "onViewCreated:tabCount "+tabLayout.tabCount)


    }

}