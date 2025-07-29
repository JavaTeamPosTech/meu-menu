    package com.postechfiap.meumenu.core.domain.presenters;

    import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

    public interface BuscarRestaurantePorIdOutputPort {
        void presentSuccess(RestauranteDomain restaurante);
    }