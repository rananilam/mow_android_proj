package com.data.model.device

enum class DeviceOptionID(val id: Int) {

    JOYSTICK_POSITION(1),
    PREFERRED_WHEEL_CHAIR_SIZE(2),
    CHAIR_PAD_REQUIREMENT(3),
    HAND_CONTROLLER(4),
    NONE(0);

    companion object {
        fun findByName(id: Int): DeviceOptionID {
            for (v in values()) {
                if (v.id == id) {
                    return v
                }
            }
            return NONE
        }
    }

}