package org.couchbase.quickstart.controllers;

import static org.couchbase.quickstart.configs.CollectionNames.PROFILE;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.couchbase.quickstart.configs.DBProperties;
import org.couchbase.quickstart.exception.BalanceException;
import org.couchbase.quickstart.exception.ProfileException;
import org.couchbase.quickstart.models.Profile;
import org.couchbase.quickstart.models.ProfileRequest;
import org.couchbase.quickstart.response.ProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.couchbase.client.java.transactions.TransactionQueryOptions;
import com.couchbase.client.java.transactions.config.TransactionOptions;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

  private final Cluster cluster;
  private final Collection profileCollection;
  private final DBProperties dbProperties;
  private final Bucket bucket;

  public ProfileController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
    System.out.println("Initializing profile controller, cluster: " + cluster + "; bucket: " + bucket);
    this.cluster = cluster;
    this.bucket = bucket;
    this.profileCollection = bucket.collection(PROFILE);
    this.dbProperties = dbProperties;
  }

  @CrossOrigin(value = "*")
  @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Create a user profile from the request")
  @ApiResponses({
      @ApiResponse(code = 201, message = "Created", response = Profile.class),
      @ApiResponse(code = 400, message = "Bad request", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
  })
  public ResponseEntity<Profile> save(@RequestBody final ProfileRequest userProfile) {
    // Generates an id, hash password and save the user
    final Profile profile = userProfile.getProfile();

    try {
      profileCollection.insert(profile.getPid(), profile);
      return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @CrossOrigin(value = "*")
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get a user profile by id", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
      })
  public ResponseEntity<Profile> getProfile(@PathVariable("id") UUID id) {
    final Profile profile = profileCollection.get(id.toString()).contentAs(Profile.class);
    return ResponseEntity.status(HttpStatus.OK).body(profile);
  }

  @CrossOrigin(value = "*")
  @PutMapping(path = "/{id}")
  @ApiOperation(value = "Update a user profile", response = Profile.class)
  @ApiResponses({
      @ApiResponse(code = 201, message = "Updated the user profile", response = Profile.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
  })
  public ResponseEntity<Profile> update(@PathVariable("id") UUID id, @RequestBody Profile profile) {
    try {
      profileCollection.upsert(id.toString(), profile);
      return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    } catch (DocumentNotFoundException dnfe) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @CrossOrigin(value = "*")
  @DeleteMapping(path = "/{id}")
  @ApiOperation(value = "Delete a user profile")
  @ApiResponses({
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 404, message = "Not Found", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
  })
  public ResponseEntity<ProfileResponse> delete(@PathVariable UUID id) {
    try {
      profileCollection.remove(id.toString());
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ProfileResponse(200, "Deleted successfully", System.currentTimeMillis()));
    } catch (DocumentNotFoundException dnfe) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ProfileResponse(404, "Not found", System.currentTimeMillis()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ProfileResponse(500, "Server Error", System.currentTimeMillis()));
    }
  }

  @CrossOrigin(value = "*")
  @GetMapping(path = "/profiles/", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Search for user profiles", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Returns the list of user profiles"),
          @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
      })
  public ResponseEntity<List<Profile>> getProfiles(
      @RequestParam(required = false, defaultValue = "5") int limit,
      @RequestParam(required = false, defaultValue = "0") int skip,
      @RequestParam String search) {

    final String qryString = "SELECT p.* FROM `" + dbProperties.getBucketName() + "`.`_default`.`" + PROFILE + "` p " +
        "WHERE lower(p.firstName) LIKE '%" + search.toLowerCase()
        + "%' OR lower(p.lastName) LIKE '%" + search.toLowerCase() + "%'  LIMIT " + limit + " OFFSET " + skip;
    System.out.println("Query=" + qryString);
    //TBD with params: final List<Profile> profiles = cluster.query("SELECT p.* FROM `$bucketName`.`_default`
    // .`$collectionName` p WHERE lower(p.firstName) LIKE '$search' OR lower(p.lastName) LIKE '$search' LIMIT $limit
    // OFFSET $skip",
    final List<Profile> profiles = cluster
        .query(qryString, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
        .rowsAs(Profile.class);

    return ResponseEntity.status(HttpStatus.OK).body(profiles);
  }

  @CrossOrigin(value = "*")
  @PostMapping(path = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Transfer credits between 2 profiles", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Returns the list of changed user profiles"),
          @ApiResponse(code = 500, message = "Error occurred while transfer operation", response = Error.class)
      })
  public ResponseEntity<List<Profile>> transferCredits(
      @RequestParam(name = "source", required = true) String sourceProfileId,
      @RequestParam(name = "target", required = true) String targetProfileId,
      @RequestParam(name = "amount", required = true) Integer amount
  ) {
    final TransactionOptions to = TransactionOptions.transactionOptions();
    final TransactionQueryOptions args = TransactionQueryOptions.queryOptions().parameters(
        JsonObject.create()
            .put("source", sourceProfileId)
            .put("amount", amount)
            .put("target", targetProfileId)
    );
    while (true) {
      try {
        cluster.transactions().run(ctx -> {
          final Profile sourceProfile = profileCollection.get(sourceProfileId).contentAs(Profile.class);
          final Profile targetProfile = profileCollection.get(targetProfileId).contentAs(Profile.class);

          if (sourceProfile == null) {
            throw new ProfileException("Source profile not found");
          }
          if (targetProfile == null) {
            throw new ProfileException("Target profile not found");
          }

          ctx.query("UPDATE `" + dbProperties.getBucketName() + "`.`_default`.`" + PROFILE
              + "` SET balance = balance - $amount WHERE pid = $source", args);
          ctx.query("UPDATE `" + dbProperties.getBucketName() + "`.`_default`.`" + PROFILE
              + "` SET balance = balance + $amount WHERE pid = $target", args);

          if (sourceProfile.getBalance() < amount) {
            throw new BalanceException("Insufficient balance");
          }
        }, to);
        break;
      } catch (Exception e) {
        if (e.getMessage().contains("DurabilityImpossible")) {
          // Sets DurabilityLevel.NONE to make it work with local clusters
          to.durabilityLevel(DurabilityLevel.NONE);
        } else {
          throw e;
        }
      }
    }
    final Profile sourceProfile = profileCollection.get(sourceProfileId).contentAs(Profile.class);
    final Profile targetProfile = profileCollection.get(targetProfileId).contentAs(Profile.class);

    return ResponseEntity.ok(Arrays.asList(sourceProfile, targetProfile));
  }

}
