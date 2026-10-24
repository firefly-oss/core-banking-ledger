package io.fireflysoftware.core.banking.ledger.sdk;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
class ManagedFundsContractTest {
    @Test
    void genuineProducerContractIncludesRecoveryAndNeverAsksForAnAuthenticatedPrincipal() throws Exception {
        String contract=Files.readString(Path.of("src/main/resources/openapi.yml"));
        assertThat(contract).contains("operationId: getFundsReservationByOperation","operationId: controlLedgerFunds","operationId: reserveFunds","operationId: expireDueFunds","operationId: reconcileFundsReservation","notAfterEpochMillis:")
            .doesNotContain("name: jwt","    Jwt:");
    }
}
