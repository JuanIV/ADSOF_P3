package tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Suite de tests que ejecuta todos los tests de la práctica 4.
 *
 * <p>
 * Agrupa los tests de los cinco apartados en una única ejecución, permitiendo
 * verificar la funcionalidad completa del sistema de estación meteorológica.
 * </p>
 *
 * @author Nombre Apellido
 */
@Suite
@SelectClasses({ TestApartado1.class, TestApartado2.class, TestApartado3.class, TestApartado4.class, TestApartado5.class })
public class TestAll {
}
