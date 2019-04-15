package com.rmnivnv.cryptomoonx.top

interface TopContract {

    interface View {

    }

    interface Presenter {
        var view: View?
    }
}