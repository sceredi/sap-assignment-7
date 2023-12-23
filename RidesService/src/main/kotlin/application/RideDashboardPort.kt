package it.unibo.sap.application

import it.unibo.sap.domain.Ride

interface RideDashboardPort {
    fun notifyOngoingRidesChanged(ongoingRides: Sequence<Ride>)
}