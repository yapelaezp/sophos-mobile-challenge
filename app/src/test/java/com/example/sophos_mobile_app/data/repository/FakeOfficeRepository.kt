package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.Office

class FakeOfficeRepository(): OfficeRepository{
    override suspend fun getOffices(): ResponseStatus<List<Office>> {
        return ResponseStatus.Success(listOf( Office(
            "Chile",
            7,
            "-70.64851440000001",
            "33.440570099999995",
            "Agustinas 833 – Piso 10"),
            Office(
                "Estados Unidos",
                8,
                "-73.98382049999998",
                "40.7504055",
                "404 Fifth Avenue Tenant LLC")
            ,
            Office(
                "Bogotá",
                3,
                "-74.044757",
                "4.6796679999999995",
                "Edificio Davivienda - Piso 4"),
            Office(
                "Medellín",
                2,
                "-75.58021739999998",
                "6.218229100000025",
                "CEOH - 107")
            ,
            Office(
                "Bogotá",
                4,
                "-74.06940610000004",
                "4.612525050021438",
                "Edificio Tequendama - Piso 30")
            ,
            Office(
                "Panamá",
                6,
                "-79.5875616999999",
                "9.005743200000007",
                "Ciudad del Saber")
            ,
            Office(
                "Medellín",
                1,
                "-75.57474939999997",
                "6.224464299999994",
                "Ciudad del Rio - 1009")
            ,
            Office(
                "México",
                5,
                "-99.16522090000001",
                "19.42736700000002",
                "Torre Reforma Latino - Piso 41")
        ))
    }
}