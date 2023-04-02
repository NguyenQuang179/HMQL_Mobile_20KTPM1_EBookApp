package com.example.hmql_ebookapp
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hmql_ebookapp.MyBooks_DownloadFragment
import com.example.hmql_ebookapp.MyBooks_LikedBooksFragment
import com.example.hmql_ebookapp.MyBooks_HistoryFragment


class MyViewPagerAdapter(fragment: Fragment, books : ArrayList<SampleBook>) : FragmentStateAdapter(fragment)
{
    var book = books
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyBooks_LikedBooksFragment(book)
            1 -> MyBooks_DownloadFragment(book)
            2 -> MyBooks_HistoryFragment(book)
            else -> MyBooks_DownloadFragment(book)
        }
    }
}

