/*
 * Copyright 2012-2015 Pellucid Analytics
 * Copyright 2015 Daniel W. H. James
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mfglabs.commons.aws
package cloudwatch

import java.util.concurrent.{Executors, ExecutorService, LinkedBlockingQueue, ThreadFactory, ThreadPoolExecutor, TimeUnit}
import scala.concurrent.{Future, Promise}
import scala.util.Try

import com.amazonaws.auth.{AWSCredentials, AWSCredentialsProvider, DefaultAWSCredentialsProviderChain}
import com.amazonaws.{AmazonWebServiceRequest, ClientConfiguration}
import com.amazonaws.internal.StaticCredentialsProvider

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient
import com.amazonaws.services.cloudwatch.model._

/**
 * A lightweight wrapper for [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/AmazonDynamoDBAsyncClient.html AmazonCloudWatchAsyncClient]].
 *
 * @constructor construct a wrapper client from an Amazon async client.
 * @param client
  *     the underlying [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/AmazonDynamoDBAsyncClient.html AmazonCloudWatchAsyncClient]].
 * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/AmazonDynamoDBAsyncClient.html AmazonCloudWatchAsyncClient]]
 */
class AmazonCloudwatchClient(val client: AmazonCloudWatchAsyncClient) {


  /**
    * make a client from a credentials provider, a config, and a default executor service.
    *
    * @param awsCredentialsProvider
    *     a provider of AWS credentials.
    * @param clientConfiguration
    *     a client configuration.
    */
  def this(awsCredentialsProvider: AWSCredentialsProvider, clientConfiguration: ClientConfiguration) = {
    this(new AmazonCloudWatchAsyncClient(
      awsCredentialsProvider,
      clientConfiguration,
      new ThreadPoolExecutor(
        0, clientConfiguration.getMaxConnections,
        60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue[Runnable],
        new AWSThreadFactory("aws.wrap.cloudwatch")))
    )
  }

  /**
    * make a client from a credentials provider, a default config, and an executor service.
    *
    * @param awsCredentialsProvider
    *     a provider of AWS credentials.
    * @param executorService
    *     an executor service for synchronous calls to the underlying AmazonS3Client.
    */
  def this(awsCredentialsProvider: AWSCredentialsProvider, executorService: ExecutorService) = {
    this(new AmazonCloudWatchAsyncClient(
      awsCredentialsProvider, new ClientConfiguration(), executorService
    ))
  }

  /**
    * make a client from a credentials provider, a default config, and a default executor service.
    *
    * @param awsCredentialsProvider
    *     a provider of AWS credentials.
    */
  def this(awsCredentialsProvider: AWSCredentialsProvider) = {
    this(awsCredentialsProvider, new ClientConfiguration())
  }

  /**
    * make a client from credentials, a config, and an executor service.
    *
    * @param awsCredentials
    *     AWS credentials.
    * @param clientConfiguration
    *     a client configuration.
    * @param executorService
    *     an executor service for synchronous calls to the underlying AmazonS3Client.
    */
  def this(awsCredentials: AWSCredentials, clientConfiguration: ClientConfiguration, executorService: ExecutorService) = {
    this(new AmazonCloudWatchAsyncClient(
      new StaticCredentialsProvider(awsCredentials), clientConfiguration, executorService
    ))
  }

  /**
    * make a client from credentials, a default config, and an executor service.
    *
    * @param awsCredentials
    *     AWS credentials.
    * @param executorService
    *     an executor service for synchronous calls to the underlying AmazonS3Client.
    */
  def this(awsCredentials: AWSCredentials, executorService: ExecutorService) = {
    this(awsCredentials, new ClientConfiguration(), executorService)
  }

  /**
    * make a client from credentials, a default config, and a default executor service.
    *
    * @param awsCredentials
    *     AWS credentials.
    */
  def this(awsCredentials: AWSCredentials) = {
    this(new StaticCredentialsProvider(awsCredentials))
  }

  /**
    * make a client from a default credentials provider, a config, and a default executor service.
    *
    * @param clientConfiguration
    *     a client configuration.
    */
  def this(clientConfiguration: ClientConfiguration) = {
    this(new DefaultAWSCredentialsProviderChain(), clientConfiguration)
  }

  /**
    * make a client from a default credentials provider, a default config, and a default executor service.
    */
  def this() = {
    this(new DefaultAWSCredentialsProviderChain())
  }

/**
  * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#deleteAlarms(com.amazonaws.services.cloudwatch.model.DeleteAlarmsRequest) AWS Java SDK]]
  */
  def deleteAlarms(
    deleteAlarmsRequest: DeleteAlarmsRequest
  ): Future[Unit] =
    wrapVoidAsyncMethod(client.deleteAlarmsAsync, deleteAlarmsRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#deleteAlarms(com.amazonaws.services.cloudwatch.model.DeleteAlarmsRequest) AWS Java SDK]]
    */
  def deleteAlarms(alarmNames: String*): Future[Unit] =
    deleteAlarms(new DeleteAlarmsRequest().withAlarmNames(alarmNames: _*))

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#describeAlarmHistory(com.amazonaws.services.cloudwatch.model.DescribeAlarmHistoryRequest) AWS Java SDK]]
    */
  def describeAlarmHistory(
    describeAlarmHistoryRequest: DescribeAlarmHistoryRequest
  ): Future[DescribeAlarmHistoryResult] =
    wrapAsyncMethod(client.describeAlarmHistoryAsync, describeAlarmHistoryRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#describeAlarmHistory() AWS Java SDK]]
    */
  def describeAlarmHistory(): Future[DescribeAlarmHistoryResult] =
    describeAlarmHistory(new DescribeAlarmHistoryRequest())

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#describeAlarms(com.amazonaws.services.cloudwatch.model.DescribeAlarmsRequest) AWS Java SDK]]
    */
  def describeAlarms(
    describeAlarmRequest: DescribeAlarmsRequest
  ): Future[DescribeAlarmsResult] =
    wrapAsyncMethod(client.describeAlarmsAsync, describeAlarmRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#describeAlarms() AWS Java SDK]]
    */
  def describeAlarms(): Future[DescribeAlarmsResult] =
    describeAlarms(new DescribeAlarmsRequest())

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#describeAlarmsForMetric(com.amazonaws.services.cloudwatch.model.DescribeAlarmsForMetricRequest) AWS Java SDK]]
    */
  def describeAlarmsForMetric(
    describeAlarmsForMetricRequest: DescribeAlarmsForMetricRequest
  ): Future[DescribeAlarmsForMetricResult] =
    wrapAsyncMethod(client.describeAlarmsForMetricAsync, describeAlarmsForMetricRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#disableAlarmActions(com.amazonaws.services.cloudwatch.model.DisableAlarmActionsRequest) AWS Java SDK]]
    */
  def disableAlarmActions(
    disableAlarmActionsRequest: DisableAlarmActionsRequest
  ): Future[Unit] =
    wrapVoidAsyncMethod(client.disableAlarmActionsAsync, disableAlarmActionsRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#disableAlarmActions(com.amazonaws.services.cloudwatch.model.DisableAlarmActionsRequest) AWS Java SDK]]
    */
  def disableAlarmActions(alarmNames: String*): Future[Unit] =
    disableAlarmActions(new DisableAlarmActionsRequest().withAlarmNames(alarmNames: _*))

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#enableAlarmActions(com.amazonaws.services.cloudwatch.model.EnableAlarmActionsRequest) AWS Java SDK]]
    */
  def enableAlarmActions(
    enableAlarmActionsRequest: EnableAlarmActionsRequest
  ): Future[Unit] =
    wrapVoidAsyncMethod(client.enableAlarmActionsAsync, enableAlarmActionsRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#enableAlarmActions(com.amazonaws.services.cloudwatch.model.EnableAlarmActionsRequest) AWS Java SDK]]
    */
  def enableAlarmActions(alarmNames: String*): Future[Unit] =
    enableAlarmActions(new EnableAlarmActionsRequest().withAlarmNames(alarmNames: _*))

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#getMetricStatistics(com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest) AWS Java SDK]]
    */
  def getMetricStatistics(
    getMetricStatisticsRequest: GetMetricStatisticsRequest
  ): Future[GetMetricStatisticsResult] =
    wrapAsyncMethod(client.getMetricStatisticsAsync, getMetricStatisticsRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#listMetrics(com.amazonaws.services.cloudwatch.model.ListMetricsRequest) AWS Java SDK]]
    */
  def listMetrics(
    listMetricsRequest: ListMetricsRequest
  ): Future[ListMetricsResult] =
    wrapAsyncMethod(client.listMetricsAsync, listMetricsRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#listMetrics() AWS Java SDK]]
    */
  def listMetrics(): Future[ListMetricsResult] =
    listMetrics(new ListMetricsRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#putMetricAlarm(com.amazonaws.services.cloudwatch.model.PutMetricAlarmRequest) AWS Java SDK]]
    */
  def putMetricAlarm(
    putMetricAlarmRequest: PutMetricAlarmRequest
  ): Future[Unit] =
    wrapVoidAsyncMethod(client.putMetricAlarmAsync, putMetricAlarmRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#putMetricData(com.amazonaws.services.cloudwatch.model.PutMetricDataRequest) AWS Java SDK]]
    */
  def putMetricData(
    putMetricDataRequest: PutMetricDataRequest
  ): Future[Unit] =
    wrapVoidAsyncMethod(client.putMetricDataAsync, putMetricDataRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#putMetricData(com.amazonaws.services.cloudwatch.model.PutMetricDataRequest) AWS Java SDK]]
    */
  def putMetricData(
    namespace:  String,
    metricData: Iterable[MetricDatum]
  ): Future[Unit] = {
    import scala.collection.JavaConversions.asJavaCollection

    putMetricData(
      new PutMetricDataRequest()
      .withNamespace(namespace)
      .withMetricData(metricData)
    )
  }

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#setAlarmState(com.amazonaws.services.cloudwatch.model.SetAlarmStateRequest) AWS Java SDK]]
    */
  def setAlarmState(
    setAlarmStateRequest: SetAlarmStateRequest
  ): Future[Unit] =
    wrapVoidAsyncMethod(client.setAlarmStateAsync, setAlarmStateRequest)

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#setAlarmState(com.amazonaws.services.cloudwatch.model.SetAlarmStateRequest) AWS Java SDK]]
    */
  def setAlarmState(
    alarmName: String,
    stateReason: String,
    stateValue: StateValue,
    stateReasonData: String = ""
  ): Future[Unit] =
    setAlarmState(
      new SetAlarmStateRequest()
      .withAlarmName(alarmName)
      .withStateReason(stateReason)
      .withStateValue(stateValue)
      .withStateReasonData(stateReasonData)
    )

  /**
    * @see [[http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/cloudwatch/AmazonCloudWatch.html#shutdown() AWS Java SDK]]
    */
  def shutdown(): Unit =
    client.shutdown()

}
