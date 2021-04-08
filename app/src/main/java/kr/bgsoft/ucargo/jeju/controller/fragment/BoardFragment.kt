package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.adapter.BoardAdapter
import kr.bgsoft.ucargo.jeju.controller.adapter.BoardKAdapter
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.http.AppHttp
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.Board
import kr.bgsoft.ucargo.jeju.data.sqlite.IsRead
import org.json.JSONArray
import org.json.JSONObject

class BoardFragment: DefaultFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    var nactivity: SubActivity? = null
    var exType: SubActivity.TYPE? = null
    var setNextIdx: Int = 0
    var setBoard: Int = 0
    var lastitemVisibleFlag = false
    var listResultEmpty = false
    var boardAdapter: BoardAdapter? = null
    var boardKAdapter: BoardKAdapter? = null
    var nRead: IsRead? = null
    var hhtype = 0
    var nswipe: SwipeRefreshLayout? = null
    var nempty: RelativeLayout? = null
    var nemptytext: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var layout = inflater.inflate(R.layout.fragment_board, container, false)

        nactivity = activity as SubActivity

        exType = activity!!.intent.getSerializableExtra(Config.EXTRA_TYPE) as SubActivity.TYPE

        when (exType) {
            SubActivity.TYPE.NOTICE -> {
                val view_title = layout.findViewById<TextView>(R.id.header_title)
                val view_back = layout.findViewById<ImageButton>(R.id.header_back)

                view_back.setOnClickListener(this)
                view_title.text = resources.getString(R.string.title_notice)
                setBoard = Config.BOARD_NOTICE
            }
            SubActivity.TYPE.NEWS -> {
                val view_title = layout.findViewById<TextView>(R.id.header_title)
                val view_back = layout.findViewById<ImageButton>(R.id.header_back)

                view_back.setOnClickListener(this)
                view_title.text = resources.getString(R.string.title_news)
                setBoard = Config.BOARD_NEWS
            }
            SubActivity.TYPE.FAQ -> {
                val view_title = layout.findViewById<TextView>(R.id.header_title)
                val view_back = layout.findViewById<ImageButton>(R.id.header_back)

                view_back.setOnClickListener(this)
                view_title.text = resources.getString(R.string.title_faq)
                setBoard = Config.BOARD_FAQ
            }
            else -> {
                layout = inflater!!.inflate(R.layout.board_list2, container, false)
                if (hhtype == 1) { //지부 공지사항
                    setBoard = Config.BOARD_JIBU
                } else { //협회 공지사항
                    setBoard = Config.BOARD_HNOTICE
                }
            }
        }

        val boardList = layout.findViewById<ListView>(R.id.board2_list)
        nswipe = layout.findViewById(R.id.board2_swipe)
        nempty = layout.findViewById(R.id.board2_empty)
        nemptytext = layout.findViewById(R.id.board2_empty_text)
        nemptytext?.text = resources.getString(R.string.list_empty2)

        nempty?.visibility = View.GONE

        nswipe?.setOnRefreshListener(this)
        nswipe?.setColorSchemeResources(
                R.color.colorListPay1,
                R.color.colorListPay2,
                R.color.colorListPay3,
                R.color.colorListPay4
        )

        nRead = IsRead(context, Config.DB_Reads, null, Config.DB_Reads_ver)

        when (exType) {
            SubActivity.TYPE.NOTICE -> {
                boardAdapter = BoardAdapter(nactivity!!)
                boardList.adapter = boardAdapter
            }
            else -> {
                boardKAdapter = BoardKAdapter(nactivity!!)
                boardList.adapter = boardKAdapter
            }
        }

        boardList.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    run {
                        var mydata = Board()

                        when (exType) {
                            SubActivity.TYPE.NOTICE -> {
                                mydata = boardAdapter!!.getItem(position)
                            }
                            else -> {
                                mydata = boardKAdapter!!.getItem(position)
                            }
                        }

                        AppHttp().getBoardView(
                                mydata.type,
                                mydata.idx,
                                Http,
                                object : DefaultHttp.Callback {
                                    override fun onError(
                                            code: DefaultHttp.HttpCode,
                                            message: String,
                                            hcode: String
                                    ) {
                                        try {
                                            toastl(resources.getString(R.string.toast_error_server))
                                        } catch (e: Exception) {

                                        }
                                    }

                                    override fun onSuccess(json: Any) {
                                        val data = json as JSONObject
                                        try {
                                            mydata.contents = data.getString("wText")
                                            UCargoActivity.showBoardView(mydata)
                                            nRead?.insert(mydata.type, mydata.idx)
                                            mydata.read = true
                                            when (exType) {
                                                SubActivity.TYPE.NOTICE -> {
                                                    boardAdapter?.dataChange()
                                                }
                                                else -> {
                                                    boardKAdapter?.dataChange()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            logd("isRead : " + e.toString())
                                            toastl(resources.getString(R.string.toast_error_server))
                                        }
                                    }
                                })
                    }
                }

        boardList.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    if (!listResultEmpty) {
                        getBoardList()
                    }
                }
            }

            override fun onScroll(
                    view: AbsListView,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
            ) {
                lastitemVisibleFlag =
                        totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }
        })

        getBoardList()

        return layout
    }

    fun getBoardList() {
        nswipe?.isRefreshing = true

        val callback = object : DefaultHttp.Callback {
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                nswipe?.isRefreshing = false
            }

            override fun onSuccess(json: Any) {
                if ((json as JSONArray).length() > 0) {
                    for (i in 0..(json.length() - 1)) {
                        val data = json.get(i) as JSONObject

                        var idx = 0
                        var title = ""
                        var date = ""
                        var type = ""
                        var read = false

                        try {
                            idx = data.getString("idx").toInt()
                            title = data.getString("title")
                            date = data.getString("regdate")
                            type = data.getString("titleType")
                            read = nRead?.select(setBoard, idx)!!

                        } catch (e: Exception) {

                        }

                        if (Config.BOARD_FAQ == setBoard) {
                            type = "Q"
                        }

                        if (idx > 0 && title.isNotBlank() && date.isNotBlank()) {
                            when (exType) {
                                SubActivity.TYPE.NOTICE -> {
                                    boardAdapter?.addData(idx, title, date, read, setBoard)
                                }
                                else -> {
                                    boardKAdapter?.addData(idx, title, date, read, setBoard, type)
                                }
                            }

                            setNextIdx = idx
                        }
                    }
                    when (exType) {
                        SubActivity.TYPE.NOTICE -> {
                            boardAdapter?.dataChange()
                        }
                        else -> {
                            boardKAdapter?.dataChange()
                        }
                    }

                    nempty?.visibility = View.GONE
                } else {
                    listResultEmpty = true

                    if (setNextIdx == 0) {
                        nempty?.visibility = View.VISIBLE
                    }
                }

                nswipe?.isRefreshing = false
            }
        }

        if (setNextIdx == 0) {
            when (exType) {
                SubActivity.TYPE.NOTICE -> {
                    boardAdapter?.clear()
                    boardAdapter?.dataChange()
                }
                else -> {
                    boardKAdapter?.clear()
                    boardKAdapter?.dataChange()
                }
            }
        }

        val id = Settings.getID(activity!!)

        when (setBoard) {
            Config.BOARD_NOTICE -> {
                AppHttp().getNoticeBoard(setNextIdx, 18, Http, callback)
            }

            Config.BOARD_NEWS -> {
               AppHttp().getNewsBoard(id, setNextIdx, 18, Http, callback)
            }
            Config.BOARD_FAQ -> {
                AppHttp().getFAQBoard(id, setNextIdx, 18, Http, callback)
            }
            else -> {
                if (hhtype == 1) {
                    if (id != null) {
                        AppHttp().getJibuBoard(id, setNextIdx, 18, Http, callback)
                    }
                } else {
                    AppHttp().getHHNoticeBoard(id, setNextIdx, 18, Http, callback)
                }
            }
        }

    }
    override fun onRefresh() {
        setNextIdx = 0
        listResultEmpty = false
        getBoardList()
    }


    override fun onClick(v: View?) {
        val id = v?.id

        when (id) {
            R.id.header_back -> {
                nactivity?.finish()
            }
        }
    }
}