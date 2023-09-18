import { Injectable } from '@angular/core';
import { S3Client, PutObjectCommand, GetObjectCommand } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class S3Service {
  private s3Client: S3Client;
  private EXPIRATION_IN_SECONDS = 43200; //12 hours
  constructor() {
    this.s3Client = new S3Client({
      region: environment.S3_REGION,
      credentials: {
        accessKeyId: environment.S3_ACCESS_KEY,
        secretAccessKey: environment.S3_SECRET_KEY,
      },
    });
  }

  async uploadImage(file: File): Promise<void> {
    const params = {
      Bucket: environment.S3_BUCKET,
      Key: 'upload/' + file.name,
      Body: file,
      ACL: 'public-read',
    };
    try {
      await this.s3Client.send(new PutObjectCommand(params));
      console.log(`File uploaded successfully to ${environment.S3_BUCKET}/${file.name}`);
    } catch (error) {
      console.error('Error uploading file:', error);
      throw error;
    }
  }

  async generatePresignedGetObjectUrl(key: String): Promise<String> {
    try {
      const imgUrl = await getSignedUrl(
        this.s3Client,
        new GetObjectCommand({
          Bucket: environment.S3_BUCKET,
          Key: key.toString(),
        }),
        { expiresIn: this.EXPIRATION_IN_SECONDS }
      );
      return imgUrl;
    } catch (error) {
      console.error('Error generating pre-signed URL for getObject:', error);
      throw error;
    }
  }
}
