package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kr.bgsoft.ucargo.jeju.controller.fragment.GamengFragment
import kr.bgsoft.ucargo.jeju.controller.fragment.MyListViewStep1Fragment
import kr.bgsoft.ucargo.jeju.controller.fragment.MyListViewStep2Fragment
import kr.bgsoft.ucargo.jeju.controller.fragment.MyListViewStep3Fragment

class MylistViewPagerAdapter (flagmanager: FragmentManager, context: Context, upload: Boolean = false, gamang: Boolean = false) : FragmentPagerAdapter(flagmanager){
    val CTX             = context
    val arrayFragment   = ArrayList<Fragment>()
    val nupload         = upload
    val ngameng         = gamang

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        if(nupload) {
            when (position) {
                1 -> {
                    fragment = MyListViewStep2Fragment()
                }
                2 -> {
                    fragment = MyListViewStep3Fragment()
                }
                else -> {
                    if(ngameng) {
                        fragment = GamengFragment()
                    } else {
                        fragment = MyListViewStep1Fragment()
                    }
                }
            }
        } else {
            when (position) {
                1 -> {
                    fragment = MyListViewStep2Fragment()
                }
                else -> {
                    if(ngameng) {
                        fragment = GamengFragment()
                    } else {
                        fragment = MyListViewStep1Fragment()
                    }
                }
            }
        }

        if((arrayFragment.size - 1) < position && fragment != null) {
            for(i in (arrayFragment.size - 1)..position) {
                arrayFragment.add(fragment)
            }
        } else {
            arrayFragment[position] = fragment
        }

        return fragment
    }

    fun getFragment(position: Int) : Fragment? {
        return arrayFragment[position]
    }

    override fun getCount(): Int {
        return if(nupload) { 3 } else { 2 }
    }
}