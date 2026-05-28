#!/bin/bash
# Navega para a pasta do projeto (garantia extra)
cd /home/pera/IdeaProjects/MERCADO-VS1 || exit 1

echo "Iniciando MERCADO-VS1 via Maven..."
# Executa o comando e mantém o terminal aberto se der erro
mvn exec:java -Dexec.mainClass="br.com.creativex.Main"

if [ $? -ne 0 ]; then
    echo ""
    echo "-------------------------------------------------------"
    echo "ERRO: Ocorreu um problema ao iniciar o sistema."
    echo "Verifique se o PostgreSQL está rodando."
    echo "-------------------------------------------------------"
    read -p "Pressione [Enter] para fechar esta janela..."
fi
