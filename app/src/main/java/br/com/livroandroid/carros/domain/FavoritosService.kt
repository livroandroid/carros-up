package br.com.livroandroid.carros.domain

import br.com.livroandroid.carros.domain.dao.DatabaseManager

object FavoritosService {
    // Retorna todos os carros favoritados
    fun getCarros(): List<Carro> {
        val dao = DatabaseManager.getCarroDAO()
        return dao.findAll()
    }
    // Verifica se um carro está favoritado
    fun isFavorito(carro: Carro) : Boolean {
        val dao = DatabaseManager.getCarroDAO()
        return dao.getById(carro.id) != null
    }

    // Salva o carro ou deleta
    fun favoritar(carro: Carro): Boolean {
        val favorito = isFavorito(carro)
        val dao = DatabaseManager.getCarroDAO()
        if(favorito) {
            // Remove dos favoritos
            dao.delete(carro)
            return false
        }
        // Adiciona nos favoritos
        dao.insert(carro)
        return true
    }

}
