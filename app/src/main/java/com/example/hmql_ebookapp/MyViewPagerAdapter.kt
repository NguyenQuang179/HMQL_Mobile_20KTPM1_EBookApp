package com.example.hmql_ebookapp
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hmql_ebookapp.MyBooks_DownloadFragment
import com.example.hmql_ebookapp.MyBooks_LikedBooksFragment
import com.example.hmql_ebookapp.MyBooks_HistoryFragment


class MyViewPagerAdapter : FragmentStateAdapter{
    constructor(fragment: Fragment) : super(fragment)

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyBooks_LikedBooksFragment()
            1 -> MyBooks_DownloadFragment()
            2 -> MyBooks_HistoryFragment()
            else -> MyBooks_DownloadFragment()
        }
    }
}

