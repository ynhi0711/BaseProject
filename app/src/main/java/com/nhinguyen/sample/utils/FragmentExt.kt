@file:Suppress("unused")

package com.nhinguyen.sample.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.doTransaction(
    func: FragmentTransaction.() ->
    FragmentTransaction
) {
    beginTransaction().func().commit()
}

fun Fragment.addFragment(fragmentManager: FragmentManager, frameId: Int, fragment: Fragment) {
    fragmentManager.doTransaction { add(frameId, fragment).addToBackStack(fragment.tag) }
}

fun Fragment.replaceFragment(
    fragmentManager: FragmentManager,
    frameId: Int,
    fragment: Fragment,
    tag: String? = null,
    sharedElement: View? = null
) {
    fragmentManager.doTransaction {
        replace(frameId, fragment)
            .also {
                if (tag != null) addToBackStack(fragment.tag)
            }
            .also {
                if (sharedElement != null) addSharedElement(
                    sharedElement,
                    sharedElement.transitionName
                )
            }
            .setReorderingAllowed(true)

    }
}