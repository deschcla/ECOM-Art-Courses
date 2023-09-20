import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class S3Service {
  private key: string;
  constructor(private httpClient: HttpClient) {}

  async uploadToS3(file: File, selectedFile: string): Promise<Observable<any>> {
    try {
      const binaryData = await this.loadImageAsBinary(file);
      selectedFile = btoa(binaryData);
      return this.httpClient.post<any>('api/produits/upload', selectedFile);
    } catch (error) {
      this.key = 'Error: ' + error;
      throw error;
    }
  }

  async getImageFromS3(key: String): Promise<Observable<any>> {
    try {
      return this.httpClient.post<any>('api/produits/images', key);
    } catch (error) {
      this.key = 'Error: ' + error;
      throw error;
    }
  }
  private loadImageAsBinary(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = event => {
        const result = event.target?.result;
        if (typeof result === 'string') {
          resolve(result);
        } else {
          reject(new Error('Failed to read image data'));
        }
      };
      reader.onerror = () => {
        reject(new Error('Error reading the image file'));
      };

      reader.readAsBinaryString(file);
    });
  }
}
