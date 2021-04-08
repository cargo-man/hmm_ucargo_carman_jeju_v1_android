package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.fragment.BoardFragment
import kr.bgsoft.ucargo.jeju.controller.fragment.HHSubFragment
import kr.bgsoft.ucargo.jeju.controller.fragment.JibuListFragment

class HHSubPagerAdapter(flagmanager: FragmentManager, context: Context, type: SubActivity.TYPE) : FragmentStatePagerAdapter(flagmanager) {

    val CTX             = context
    val type            = type
    var isChange        = false

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(type) {
            SubActivity.TYPE.HH2 -> {
                fragment = HHSubFragment()
                when(position) {
                    1 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE2_2
                    }
                    /*2 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE2_3
                    }*/
                    else -> {
                        fragment.sub = HHSubFragment.SUB.PAGE2_1
                    }
                }
            }
            SubActivity.TYPE.HH3 -> {
                fragment = HHSubFragment()
                when(position) {
                    1 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE3_1
                    }
                    2 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE3_2
                    }
                    3 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE3_3
                    }
                    4 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE3_4
                    }
                    5 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE3_5
                    }
                    6 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE3_6
                    }
                    else -> {
                        fragment.sub = HHSubFragment.SUB.PAGE3_0
                    }
                }
            }
            SubActivity.TYPE.HH4 -> {
                when(position) {
                    1 -> {
                        fragment = JibuListFragment()
                    }
                    else -> {
                        fragment = HHSubFragment()
                        fragment.sub = HHSubFragment.SUB.PAGE4_1
                    }
                }
            }
            SubActivity.TYPE.HH5 -> {
                fragment = BoardFragment()

                when(position) {
                    1 -> {
                        fragment.hhtype = 1
                    }
                    else -> {
                        fragment.hhtype = 2
                    }
                }
            }
            else -> {
                fragment = HHSubFragment()
                when(position) {
                    1 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE1_2
                    }
                    2 -> {
                        fragment.sub = HHSubFragment.SUB.PAGE1_3
                    }
                    else -> {
                        fragment.sub = HHSubFragment.SUB.PAGE1_1
                    }
                }
            }
        }

        return fragment!!
    }
    override fun getCount(): Int {
        return when(type) {
            SubActivity.TYPE.HH2 -> {
                3
            }
            SubActivity.TYPE.HH3 -> {
                7
            }
            SubActivity.TYPE.HH4 -> {
                2
            }
            SubActivity.TYPE.HH5 -> {
                2
            }
            else -> {
                3
            }
        }
    }

    override fun getItemPosition(o: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}