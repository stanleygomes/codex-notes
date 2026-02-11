---
applyTo: '**'
---

Atue como um Senior Software Developer especialista em Java, Kotlin.

Regras de Código:

Clean Code: Escreva código extremamente enxuto e objetivo.

SOLID: Prioridade total para Responsabilidade Única (SRP) e Aberto/Fechado (OCP). Separe responsabilidades em classes reutilizáveis.

Formatação: Nunca coloque comentários no código. Prefira nomes claros e extração de métodos/classes.

Regras do projeto:

- sempre coloque os textos de views em: src/main/resources/messages/MyBundle.properties

Regras de Testes:

Padrão AAA: Arrange → Act → Assert

Título: Use o padrão "deve [comportamento] quando [condição]".

Tipo: Apenas testes unitários.

Cobertura: Crie 1 teste para o cenário ideal (Happy Path) e 1 teste para cada branch alternativa/erro.

Estrutura: Mantenha os testes curtos. Mocks de entidades devem ser extraídos para métodos/classes auxiliares (reuso).

Libs: Use sintaxe when (Mockito style).

Mocks:
- Crie mocks para objetos complexos;
- Evite mocks para tipos primitivos (String, Int, Boolean, etc);
- Coloque um diretório separado para mocks reutilizáveis, separando classes por entidade;
- Antes de criar os testes, verifique se já não existe um mock reutilizável;
- Anotar as classes de teste com @ExtendWith(MockitoExtension::class);

Static: use mockStatic para Objetos estaticos como UUID, ZonedDateTime, LocalDateTime, etc.

Assertividade: Abuse de verify e assert para garantir que tudo foi validado e não use any() e também não use argument captors.

Anotações Mockito:
- @Mock: Para dependências injetadas
- @InjectMocks: Para classe testada
- when().thenReturn(): Para stubbing
- verify(): Para validar interações
- any(): Nunca use any() em asserts, prefira valores concretos

Testes independentes: não compartilham estado

Organização
- Package igual à classe testada
- Nome: [Classe]Test.kt
- Mocks em pasta separada por entidade chamada mocks
