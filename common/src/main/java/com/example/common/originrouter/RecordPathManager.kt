package com.example.common.originrouter

object RecordPathManager {

    private var groupMap = mutableMapOf<String, ArrayList<PathBean>>()

    fun <T> joinGroup(groupName: String, pathName: String, clazz: Class<T>) {
        var pathBeans = groupMap[groupName]
        if (pathBeans == null) {
            pathBeans = ArrayList()
            pathBeans.add(PathBean(pathName, clazz))
            groupMap[groupName] = pathBeans
        } else {
            pathBeans.forEach {
                if (pathName != it.path) {
                    pathBeans.add(PathBean(pathName, clazz))
                    groupMap[groupName] = pathBeans
                }
            }
        }
    }

    fun getTargetClass(groupName: String, pathName: String): Class<*>? {
        val pathBeans = groupMap[groupName]
        pathBeans?.forEach {
            if (it.path == pathName) {
                return it.clazz
            }
        }
        return null
    }


}