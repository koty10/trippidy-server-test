package dev.koten.trippidy.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public class MemberDto implements Serializable {
    private String id;
    private String role;
    private boolean accepted;
    private Collection<ItemDto> items;
    private String tripId;
    private String userProfileId;
    private String userProfileFirstname;
    private String userProfileLastname;
    private String userProfileImage;
    private Collection<FutureTransactionDto> futureTransactions;
    private Collection<CompletedTransactionDto> completedTransactionsSent;
    private Collection<CompletedTransactionDto> completedTransactionsReceived;
    private String userProfileBankAccountNumber;
    private String userProfileIban;
    private String userProfileEmail;

    public MemberDto(String id, String role, boolean accepted, Collection<ItemDto> items, String tripId, String userProfileId, String userProfileFirstname, String userProfileLastname, String userProfileImage,
                     Collection<FutureTransactionDto> futureTransactions,
                     Collection<CompletedTransactionDto> completedTransactionsSent,
                     Collection<CompletedTransactionDto> completedTransactionsReceived, String userProfileBankAccountNumber, String userProfileIban, String userProfileEmail) {
        this.id = id;
        this.role = role;
        this.accepted = accepted;
        this.items = items;
        this.tripId = tripId;
        this.userProfileId = userProfileId;
        this.userProfileFirstname = userProfileFirstname;
        this.userProfileLastname = userProfileLastname;
        this.userProfileImage = userProfileImage;
        this.futureTransactions = futureTransactions;
        this.completedTransactionsSent = completedTransactionsSent;
        this.completedTransactionsReceived = completedTransactionsReceived;
        this.userProfileBankAccountNumber = userProfileBankAccountNumber;
        this.userProfileIban = userProfileIban;
        this.userProfileEmail = userProfileEmail;
    }

    public MemberDto() {
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public boolean getAccepted() {
        return accepted;
    }

    public Collection<ItemDto> getItems() {
        return items;
    }

    public String getTripId() {
        return tripId;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public String getUserProfileFirstname() {
        return userProfileFirstname;
    }

    public String getUserProfileLastname() {
        return userProfileLastname;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setItems(Collection<ItemDto> items) {
        this.items = items;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public void setUserProfileFirstname(String userProfileFirstname) {
        this.userProfileFirstname = userProfileFirstname;
    }

    public void setUserProfileLastname(String userProfileLastname) {
        this.userProfileLastname = userProfileLastname;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto entity = (MemberDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.role, entity.role) &&
                Objects.equals(this.accepted, entity.accepted) &&
                Objects.equals(this.items, entity.items) &&
                Objects.equals(this.futureTransactions, entity.futureTransactions) &&
                Objects.equals(this.tripId, entity.tripId) &&
                Objects.equals(this.userProfileId, entity.userProfileId) &&
                Objects.equals(this.userProfileFirstname, entity.userProfileFirstname) &&
                Objects.equals(this.userProfileImage, entity.userProfileImage) &&
                Objects.equals(this.userProfileBankAccountNumber, entity.userProfileBankAccountNumber) &&
                Objects.equals(this.userProfileIban, entity.userProfileIban) &&
                Objects.equals(this.userProfileEmail, entity.userProfileEmail) &&
                Objects.equals(this.userProfileLastname, entity.userProfileLastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, accepted, items, tripId, userProfileId, userProfileFirstname, userProfileLastname, userProfileEmail, userProfileImage, userProfileBankAccountNumber, userProfileIban);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "role = " + role + ", " +
                "accepted = " + accepted + ", " +
                "items = " + items + ", " +
                "futureTransactions = " + futureTransactions + ", " +
                "tripId = " + tripId + ", " +
                "userProfileId = " + userProfileId + ", " +
                "userProfileFirstname = " + userProfileFirstname + ", " +
                "userProfileLastname = " + userProfileLastname + ", " +
                "userProfileEmail = " + userProfileEmail + ", " +
                "userProfileImage = " + userProfileImage + ", " +
                "userProfileBankAccountNumber = " + userProfileBankAccountNumber + ", " +
                "userProfileIban = " + userProfileIban + ")";
    }

    public Collection<FutureTransactionDto> getFutureTransactions() {
        return futureTransactions;
    }

    public void setFutureTransactions(Collection<FutureTransactionDto> futureTransactions) {
        this.futureTransactions = futureTransactions;
    }

    public Collection<CompletedTransactionDto> getCompletedTransactionsSent() {
        return completedTransactionsSent;
    }

    public void setCompletedTransactionsSent(Collection<CompletedTransactionDto> completedTransactionsSent) {
        this.completedTransactionsSent = completedTransactionsSent;
    }

    public Collection<CompletedTransactionDto> getCompletedTransactionsReceived() {
        return completedTransactionsReceived;
    }

    public void setCompletedTransactionsReceived(Collection<CompletedTransactionDto> completedTransactionsReceived) {
        this.completedTransactionsReceived = completedTransactionsReceived;
    }

    public String getUserProfileBankAccountNumber() {
        return userProfileBankAccountNumber;
    }

    public String getUserProfileIban() {
        return userProfileIban;
    }

    public void setUserProfileBankAccountNumber(String userProfileBankAccountNumber) {
        this.userProfileBankAccountNumber = userProfileBankAccountNumber;
    }

    public void setUserProfileIban(String userProfileIban) {
        this.userProfileIban = userProfileIban;
    }

    public String getUserProfileEmail() {
        return userProfileEmail;
    }

    public void setUserProfileEmail(String userProfileEmail) {
        this.userProfileEmail = userProfileEmail;
    }
}