package it.unibo.sap.application

import it.unibo.sap.domain.Ride

/**
 * Port used to notify the dashboard of ongoing rides changes.
 */
interface RideDashboardPort {
    fun notifyOngoingRidesChanged(ongoingRides: Sequence<Ride>)
}