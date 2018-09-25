# thinksim
Simulador de kanban com analise sentimental

# Tutorial de implantação

Requisitos
- JDK e JRE8
- PostgreSQL 9.4 
- Netbeans ou IDE Java 
- Tomcat 7 

1. Certifique que o JDK 8 esteja instalado corretamente. No Linux veja se está incluido no arquivo .profile o path correto, algo semelhante a linha abaixo

		export PATH=$HOME/bin:$HOME/work/java/jdk/bin:$PATH

2. Baixe o projeto usando o git ou via download 
4. Por ser um projeto maven, o Netbeans deve reconhecer e abrir o projeto normalmente
5. Antes de implantar no Tomcat, faça a restauração do Banco de dados usando o PgAdmin3 ou 4
		
		- Crie um banco de dados com o nome de thinksim via Pgadmin
		- Restaure o banco de dados através do arquivo na pasta thinksim/src/main/resources/thinksim-1.backup
		- Altere no arquivo persistence.xml a senha que foi definida para o acesso ao banco de dados
6. No Netbeans clique em Limpar e Construir e após o projeto ser implantado, deverá ser aberto no Google Chrome






