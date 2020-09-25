package com.tenisme.stickyheaderswithoutlibrary

/**
 * Created By Lonnie on 2020/05/08
 *
 */

// 여러 타입의 뷰를 아이템으로 가진 리스트이다.
// Data 타입과 Int 타입의 매개변수를 가진 AdapterItem 이라는 data class 를 만들어 준다.
data class AdapterItem(var type: Int, var objects: Data)