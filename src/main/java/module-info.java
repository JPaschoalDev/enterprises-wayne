module com.wayne.wayneen.enterpriseswyne {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;

    // JDK
    requires java.sql;
    requires java.desktop;

    // Bibliotecas externas
    requires itextpdf;
    requires org.apache.poi.ooxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.mail;

    uses java.sql.Driver;

    exports com.wayne.wayneen.enterpriseswyne;
    opens  com.wayne.wayneen.enterpriseswyne to javafx.base, javafx.fxml;
}
