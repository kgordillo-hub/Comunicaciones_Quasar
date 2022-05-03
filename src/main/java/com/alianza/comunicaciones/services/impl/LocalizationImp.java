package com.alianza.comunicaciones.services.impl;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.stereotype.Service;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.models.Coordinates;
import com.alianza.comunicaciones.services.ILocalizationService;
import com.alianza.comunicaciones.utils.Constants;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

/**
 * Implementation of ILocalizationService, to determine the space ship position
 * 
 * @author ksgor
 *
 */
@Service
public class LocalizationImp implements ILocalizationService {

	/**
	 * Method to get the spaceship position
	 * 
	 * @param the array of distances to the satellite
	 * @param the matrix of initial positions (x,y) of the satellites
	 */
	@Override
	public Coordinates getLocation(final double[] distancesToSatellite, final double[][] initialPositions)
			throws SystemException, BusinessException {
		final double[] shipPosition = calculateTrilateration(distancesToSatellite, initialPositions);
		return new Coordinates(shipPosition[0], shipPosition[1]);
	}

	/**
	 * Method to calculate the position using trilateration
	 * 
	 * @param distances to satellite
	 * @param positions of the satellites
	 * @return The spaceship position if possible
	 * @throws SystemException   if there is an error in the computation
	 * @throws BusinessException If distances are null or not equal to the number of
	 *                           satellites to hit
	 */
	private double[] calculateTrilateration(final double[] distances, final double[][] positions)
			throws BusinessException {
		if (validateArraySize(distances, positions)) {
			final TrilaterationFunction tlf = new TrilaterationFunction(positions, distances);
			final LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
			final NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(tlf, optimizer);
			final Optimum optimum = solver.solve();
			double[] centroid = optimum.getPoint().toArray();
			return centroid;
		} else {
			throw new BusinessException(Constants.SHIP_POSITION_NULL);
		}

	}

	/**
	 * Validates if the distances to the satellite and the spaceship distances are
	 * the same
	 * 
	 * @param distances to satellite
	 * @param satellite positions
	 * @return true if they have the same size. False if not.
	 */
	private boolean validateArraySize(final double[] distances, final double[][] positions) {
		if (positions != null && distances != null && positions.length > 0 && distances.length > 0
				&& positions.length == distances.length) {
			return true;
		} else {
			return false;
		}
	}

}
